/*
 * Copyright 2015-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ameba.oauth2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static java.lang.String.format;

/**
 * A DefaultTokenExtractor validates the issuer of a JWT against a whitelist, parses the
 * JWT using the proper algorithm to validate the signature.
 *
 * @author Heiko Scherrer
 */
public class DefaultTokenExtractor implements TokenExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTokenExtractor.class);
    private final IssuerWhiteList<Issuer> whiteList;
    private final List<TokenParser> parsers;

    public DefaultTokenExtractor(IssuerWhiteList whiteList, List<TokenParser> parsers) {
        this.whiteList = whiteList;
        this.parsers = parsers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtractionResult canExtract(String token) {
        String[] parts = token.split("\\.");
        return parts.length == 3 ? new ExtractionResult() : new ExtractionResult("Not a valid JWT");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtractionResult extract(final String token) {
        // we do not trust the signature so first parse the token and check the issuer
        String[] splitToken = token.split("\\.");
        if (splitToken.length < 2) {
            throw new InvalidTokenException("Token is not a JWT");
        }
        Jwt<Header, Claims> jwt;
        try {
            jwt = Jwts.parser()
                    .setAllowedClockSkewSeconds(Issuer.DEFAULT_MAX_SKEW_SECONDS)
                    .parse(splitToken[0] + "." + splitToken[1] + ".");
        } catch (ExpiredJwtException e) {
            LOGGER.error(e.getMessage(), e);
            throw new InvalidTokenException("Token has expired");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new InvalidTokenException("Token cannot be parsed");
        }

        Issuer issuer;
        if (jwt.getHeader().containsKey("kid")) {

            issuer = whiteList.getIssuer(jwt.getBody().getIssuer(), ""+jwt.getHeader().get("kid"));
        } else {

            List<Issuer> issuers = whiteList.getIssuers(jwt.getBody().getIssuer());
            // Okay, the issuer seems to have multiple kids for the same issuer ID, so take the first one...
            issuer = issuers.isEmpty() ? null : issuers.get(0);
        }

        if (issuer == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Issuer not accepted");
            }
            throw new InvalidTokenException("Issuer not accepted");
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Issuer accepted [{}]", issuer.getIssuerId());
            }
        }

        // Now check with Signature
        ObjectMapper om = new ObjectMapper();
        try {
            JsonNode jsonNode = om.readValue(TextCodec.BASE64URL.decodeToString(token), JsonNode.class);
            if (jsonNode.has("alg")) {
                TokenParser parser = parsers.stream()
                        .filter(p -> jsonNode.get("alg").asText().equals(p.supportAlgorithm()))
                        .findFirst()
                        .orElseThrow(() -> new InvalidTokenException(format("Algorithm [%s] not supported", jsonNode.get("alg"))));
                return new ExtractionResult(parser.parse(token, issuer));
            }
            return new ExtractionResult("No alg claim defined in JWT header");
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new InvalidTokenException("Token cannot be parsed into JSON");
        }
    }
}

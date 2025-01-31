/*
 * Copyright 2015-2024 the original author or authors.
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
import io.jsonwebtoken.io.Decoders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
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
    private final ObjectMapper om = new ObjectMapper();
    private final IssuerWhiteList<Issuer> whiteList;
    private final List<TokenParser> parsers;

    /**
     * {@inheritDoc}
     */
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
        return parts.length == 3 ? new ExtractionResult() : new ExtractionResult("Not a valid JWT, expected 3 parts");
    }

    private JsonNode parse(String token, ObjectMapper om) {
        try {
            return om.readValue(Decoders.BASE64URL.decode(token), JsonNode.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new InvalidTokenException("Part of token cannot be parsed");
        }
    }

    private void ensureTokenNotExpired(JsonNode tokenPayload) {
        long nowTime = new Date().getTime();
        var exp = tokenPayload.get("exp").isNull() ? null : new Date(tokenPayload.get("exp").asLong() * 1000);
        if (exp != null) {
            var maxTime = nowTime - Issuer.DEFAULT_MAX_SKEW_SECONDS;
            var max = new Date(maxTime);
            if (max.after(exp)) {
                throw new InvalidTokenException("Token has expired");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtractionResult extract(final String token) {
        // we do not trust the signature so first parse the token and check the issuer
        var splitToken = token.split("\\.");
        if (splitToken.length < 2) {
            throw new InvalidTokenException("Token is not a JWT");
        }

        var tokenHeader = parse(splitToken[0], om);
        var tokenPayload = parse(splitToken[1], om);

        ensureTokenNotExpired(tokenPayload);

        Issuer issuer;
        if (tokenHeader.has("kid")) {

            issuer = whiteList.getIssuer(tokenPayload.get("iss").asText(), tokenHeader.get("kid").asText());
        } else {

            var issuers = whiteList.getIssuers(tokenPayload.get("iss").asText());
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
        if (tokenHeader.has("alg")) {
            final var alg = tokenHeader.get("alg").asText();
            var parser = parsers.stream()
                    .filter(p -> alg.equals(p.supportAlgorithm()))
                    .findFirst()
                    .orElseThrow(() -> new InvalidTokenException(format("Algorithm [%s] not supported", alg)));
            return new ExtractionResult(parser.parse(token, issuer));
        }
        return new ExtractionResult("No alg claim defined in JWT header");
    }
}

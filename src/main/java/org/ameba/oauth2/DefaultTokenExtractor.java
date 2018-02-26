/*
 * Copyright 2014-2015 the original author or authors.
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
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

/**
 * A DefaultTokenExtractor validates the issuer of a JWT against a whitelist, parses the
 * JWT using the proper algorithm to validate the signature.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
public class DefaultTokenExtractor implements TokenExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTokenExtractor.class);
    public static final int MAX_SKEW_SECONDS = 2592000;
    private final IssuerWhiteList whiteList;
    private final List<TokenParser> parsers;

    @Inject
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
        // we do not trust the signature so first parse the token an check the issuer
        String[] splitToken = token.split("\\.");
        Jwt<Header, Claims> jwt = Jwts.parser()
                .setAllowedClockSkewSeconds(MAX_SKEW_SECONDS)
                .parse(splitToken[0] + "." + splitToken[1] + ".");

        Optional<Issuer> optIssuer = whiteList.getIssuer(jwt.getBody().getIssuer());
        if (!optIssuer.isPresent()) {
            throw new InvalidTokenException("Token issuer is not known and therefor rejected");
        }
        Issuer issuer = optIssuer.get();
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("Issuer accepted [{}]", issuer.getIssuerId());
        }

        // Now check with Signature
        ObjectMapper om = new ObjectMapper();
        try {
            JsonNode jsonNode = om.readValue(TextCodec.BASE64URL.decodeToString(token), JsonNode.class);
            if (jsonNode.has("alg")) {
                TokenParser parser = parsers.stream().filter(p -> p.supportAlgorithm().equals(jsonNode.get("alg").asText())).findFirst().orElseThrow(()->new InvalidTokenException(format("Algorithm [%s] not supported", jsonNode.get("alg"))));
                return new ExtractionResult(parser.parse(token, issuer));
            }
            return new ExtractionResult();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new InvalidTokenException("Token cannot be parsed into JSON");
        }
    }
}

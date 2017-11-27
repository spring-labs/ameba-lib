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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

/**
 * A BearerTokenExtractor tries to extract the Bearer token from an authorization header.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
public class BearerTokenExtractor implements TokenExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BearerTokenExtractor.class);
    private final IssuerWhiteList whiteList;
    private final List<TokenParser> parsers;
    private Map<String, TokenParser<Issuer, ?>> parserMap = new ConcurrentHashMap<>();

    @Inject
    private BearerTokenExtractor(IssuerWhiteList whiteList, List<TokenParser> parsers) {
        this.whiteList = whiteList;
        this.parsers = parsers;
    }

    @PostConstruct
    private void onPostConstruct() {
        parsers.stream().map(p->parserMap.put(p.supportAlgorithm(), p));
    }

    /**
     * {@inheritDoc}
     *
     * This implementation supports the 'Bearer' authorization scheme.
     */
    @Override
    public ExtractionResult canExtract(String authHeader) {
        String token = stripBearer(authHeader);
        String[] parts = token.split("\\.");
        return authHeader.startsWith("Bearer ") && parts.length == 3 ? new ExtractionResult() : new ExtractionResult("Not a valid JWT");
    }

    private String stripBearer(String authHeader) {
        return authHeader.replace("Bearer ", "");
    }

    /**
     * {@inheritDoc}
     *
     * This implementation expects a JWT as Bearer Token. It parses the JWT and validates
     * the signature of the JWT.
     */
    @Override
    public ExtractionResult extract(final String authHeader) {
        if (!canExtract(authHeader).isExtractionPossible()) {
            throw new InvalidTokenException("Not a valid JWT");
        }
        String token = stripBearer(authHeader);
        // we do not trust the signature so first parse the token an check the issuer
        String[] splitToken = token.split("\\.");
        Jwt<JwsHeader,Claims> jwt = Jwts.parser().setAllowedClockSkewSeconds(2592000).parse(splitToken[0] + "." + splitToken[1] + ".");

        Optional<Issuer> optIssuer = whiteList.getIssuer(jwt.getBody().getIssuer());
        if (!optIssuer.isPresent()) {
            throw new InvalidTokenException("Token issuer is not known and therefor rejected");
        }
        Issuer issuer = optIssuer.get();
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("Issuer accepted [{}]", issuer.getIssuerId());
        }

        // Now check with Signature
        TokenParser<Issuer, ?> parser = parserMap.get(jwt.getHeader().getAlgorithm());
        if (parser == null) {
            throw new InvalidTokenException(format("Algorithm [%s] not supported", jwt.getHeader().getAlgorithm()));
        }
        return new ExtractionResult(parser.parse(token, issuer));
    }
}

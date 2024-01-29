/*
 * Copyright 2005-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ameba.http.identity;

import io.jsonwebtoken.Claims;
import org.ameba.oauth2.ExtractionResult;
import org.ameba.oauth2.TokenExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

import static org.ameba.Constants.HEADER_VALUE_X_IDENTITY;

/**
 * A TokenResolverStrategy.
 *
 * @author Heiko Scherrer
 */
public class TokenResolverStrategy implements IdentityResolverStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenResolverStrategy.class);
    private ServiceLoader<TokenExtractor> tokenExtractorServiceLoader = ServiceLoader.load(TokenExtractor.class);
    private TokenExtractor tokenExtractor;

    public TokenResolverStrategy() {
        if (tokenExtractorServiceLoader.iterator().hasNext()) {
            this.tokenExtractor = tokenExtractorServiceLoader.iterator().next();
        } else {
            //this.tokenExtractor = new DefaultTokenExtractor();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Identity> getIdentity(Map<String, List<String>> headers, Map<String, String> bodyParts, Map<String, String> queryParams) {
        List<String> identity = headers.get(HEADER_VALUE_X_IDENTITY);
        if (identity == null || identity.size() != 1) {
            LOGGER.debug("No [{}] header set", HEADER_VALUE_X_IDENTITY);
            return Optional.empty();
        }
        ExtractionResult extract;
        try {
            extract = tokenExtractor.extract(identity.get(0));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Optional.empty();
        }
        Map<String, Object> map = new HashMap<>();
        ((Claims) extract.getJwt().getBody()).entrySet().iterator().forEachRemaining(a -> map.put(a.getKey(), a.getValue()));
        Object name = map.get(Claims.SUBJECT);
        if (name == null) {
            LOGGER.warn("No subject claim found in token");
            return Optional.empty();
        }
        Integer exp = (Integer) map.get(Claims.EXPIRATION);
        if (exp == null || exp < (System.currentTimeMillis()/1000)) {
            LOGGER.error("Token expired, claim exp = [{}]", exp);
            return Optional.empty();
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Extracted identity [{}] from token", name);
        }
        return Optional.of(new SimpleIdentity(name.toString()));
    }
}

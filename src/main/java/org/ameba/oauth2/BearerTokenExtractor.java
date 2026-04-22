/*
 * Copyright 2015-2026 the original author or authors.
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

import javax.inject.Inject;
import java.util.List;

/**
 * A BearerTokenExtractor tries to extract the Bearer token from an authorization header.
 *
 * @author Heiko Scherrer
 */
public class BearerTokenExtractor extends DefaultTokenExtractor {

    @Inject
    public BearerTokenExtractor(IssuerWhiteList whiteList, List<TokenParser> parsers) {
        super(whiteList, parsers);
    }

    /**
     * {@inheritDoc}
     *
     * This implementation supports the 'Bearer' authorization scheme.
     */
    @Override
    public ExtractionResult canExtract(String authHeader) {
        if (authHeader == null) {
            return new ExtractionResult("No Authorization header");
        }
        if (!authHeader.startsWith("Bearer ")) {
            return new ExtractionResult("Not a valid JWT");
        }
        var token = stripBearer(authHeader);
        var parts = token.split("\\.");
        if (parts.length != 3 || token.indexOf(' ') >= 0) {
            return new ExtractionResult("Not a valid JWT");
        }
        return new ExtractionResult();
    }

    private String stripBearer(String authHeader) {
        return authHeader.startsWith("Bearer ") ? authHeader.substring("Bearer ".length()) : authHeader;
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
            throw new InvalidTokenException("Not a valid Bearer token");
        }
        return super.extract(stripBearer(authHeader));
    }
}

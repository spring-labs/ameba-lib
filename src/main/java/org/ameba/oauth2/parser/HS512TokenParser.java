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
package org.ameba.oauth2.parser;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.ameba.oauth2.InvalidTokenException;
import org.ameba.oauth2.Symmetric;
import org.ameba.oauth2.TokenParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A HS256TokenParser uses a symmetric SHA-512 signing key to verify the signature.
 *
 * @author Heiko Scherrer
 */
public class HS512TokenParser implements TokenParser<Symmetric, Jwt> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HS512TokenParser.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public String supportAlgorithm() {
        return SignatureAlgorithm.HS512.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Jwt parse(String token, Symmetric issuer) {
        if (issuer == null) {
            throw new IllegalArgumentException("Expected symmetric issuer is null");
        }
        if (issuer.getSigningKey() == null || "".equals(issuer.getSigningKey())) {
            throw new IllegalArgumentException("Symmetric signing key is null or empty. Configure a signing key");
        }

        Jwt jwt;
        try {
            jwt = Jwts.parser()
                    .clockSkewSeconds(issuer.getSkewSeconds())
                    .setSigningKey(issuer.getSigningKey())
                    .build().parseClaimsJws(token);
            return jwt;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new InvalidTokenException(e.getMessage());
        }
    }
}

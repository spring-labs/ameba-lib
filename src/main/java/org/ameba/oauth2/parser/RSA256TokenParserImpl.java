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
package org.ameba.oauth2.parser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.ameba.oauth2.Asymmetric;
import org.ameba.oauth2.InvalidTokenException;
import org.ameba.oauth2.TokenParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * A RSA256TokenParserImpl uses a SHA-256 Public Key to verify signature.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
public class RSA256TokenParserImpl implements TokenParser<Asymmetric, Jws<Claims>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSA256TokenParserImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public String supportAlgorithm() {
        return SignatureAlgorithm.RS256.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Jws<Claims> parse(String token, Asymmetric issuer) {

        Jws<Claims> jws;
        try {
            byte[] publicKeyBytes = Base64.getDecoder().decode(issuer.getPublicKey());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            jws = Jwts.parser()
                    .setAllowedClockSkewSeconds(issuer.getSkewSeconds())
                    .setSigningKey(pubKey)
                    .parseClaimsJws(token);
            return jws;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new InvalidTokenException(e.getMessage());
        }
    }
}

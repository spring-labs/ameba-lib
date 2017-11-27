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
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;

/**
 * A AccessTokenExtractor.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
@Component
class AccessTokenExtractor implements TokenExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenExtractor.class);
    private final TokenIssuerWhiteList whiteList;

    private AccessTokenExtractor(TokenIssuerWhiteList whiteList) {
        this.whiteList = whiteList;
    }

    @Override
    public ExtractionResult canExtract(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String[] parts = token.split("\\.");
        return authHeader.startsWith("Bearer ") && parts.length == 3 ? new ExtractionResult() : new ExtractionResult("Not a valid JWT");
    }

    @Override
    public ExtractionResult extract(final String authHeader) {
        if (!canExtract(authHeader).isExtractionPossible()) {
            throw new InvalidTokenException("Not a valid JWT");
        }
        String token = authHeader.replace("Bearer ", "");
        // we do not trust the signature so first parse the token an check the issuer
        String[] splitToken = token.split("\\.");
        Jwt<JwsHeader,Claims> jwt = Jwts.parser().setAllowedClockSkewSeconds(2592000).parse(splitToken[0] + "." + splitToken[1] + ".");
        Optional<Issuer> optIssuer = whiteList.getIssuer(jwt.getBody().getIssuer());

        if (!optIssuer.isPresent()) {
            throw new InvalidTokenException("Token issuer is not known and therefor rejected");
        }
        Issuer issuer = optIssuer.get();
        LOGGER.debug("Issuer accepted [{}]", issuer.getIssuerId());
        Jws jws;
        try {
            byte[] publicBytes = Base64.getDecoder().decode(issuer.getSigningKey());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            // Now check with Signature
            jws = Jwts.parser()
                    .setAllowedClockSkewSeconds(issuer.getSkewSeconds())
                    .setSigningKey(pubKey)
                    .parseClaimsJws(token);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new InvalidTokenException(e.getMessage());
        }
        return new ExtractionResult(jws);
    }
}

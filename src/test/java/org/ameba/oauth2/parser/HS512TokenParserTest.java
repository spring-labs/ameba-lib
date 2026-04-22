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
package org.ameba.oauth2.parser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.ameba.oauth2.InvalidTokenException;
import org.ameba.oauth2.Issuer;
import org.ameba.oauth2.Symmetric;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.security.SecureRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * A HS512TokenParserTest verifies the guards of the symmetric HMAC-SHA-512 token parser
 * and pins its {@link org.ameba.oauth2.TokenParser#supportAlgorithm()} contract.
 *
 * @author Heiko Scherrer
 */
class HS512TokenParserTest {

    private final HS512TokenParser testee = new HS512TokenParser();

    @Test void supportAlgorithmIsHS512() {
        assertThat(testee.supportAlgorithm()).isEqualTo("HS512");
    }

    @Test void parseRejectsNullIssuer() {
        assertThatThrownBy(() -> testee.parse("x.y.z", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("symmetric issuer is null");
    }

    @Test void parseRejectsIssuerWithNullSigningKey() {
        var issuer = new TestSymmetric(null);

        assertThatThrownBy(() -> testee.parse("x.y.z", issuer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("signing key is null or empty");
    }

    @Test void parseRejectsIssuerWithEmptySigningKey() {
        var issuer = new TestSymmetric("");

        assertThatThrownBy(() -> testee.parse("x.y.z", issuer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("signing key is null or empty");
    }

    @Test void parseReturnsJwtForValidSignedToken() {
        var keyBytes = randomHmacSha512KeyBytes();
        var base64Key = Encoders.BASE64.encode(keyBytes);
        var token = Jwts.builder()
                .subject("test-subject")
                .signWith(Keys.hmacShaKeyFor(keyBytes))
                .compact();

        var jwt = testee.parse(token, new TestSymmetric(base64Key));

        assertThat(jwt).isNotNull();
        var claims = (Claims) jwt.getPayload();
        assertThat(claims.getSubject()).isEqualTo("test-subject");
    }

    @Test void parseRejectsTokenSignedWithDifferentKey() {
        var signingKeyBytes = randomHmacSha512KeyBytes();
        var verifyingKeyBytes = randomHmacSha512KeyBytes();
        var token = Jwts.builder()
                .subject("x")
                .signWith(Keys.hmacShaKeyFor(signingKeyBytes))
                .compact();
        var issuer = new TestSymmetric(Encoders.BASE64.encode(verifyingKeyBytes));

        assertThatThrownBy(() -> testee.parse(token, issuer))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test void parseRejectsMalformedToken() {
        var issuer = new TestSymmetric(Encoders.BASE64.encode(randomHmacSha512KeyBytes()));

        assertThatThrownBy(() -> testee.parse("not.a.real.jwt", issuer))
                .isInstanceOf(InvalidTokenException.class);
    }

    private static byte[] randomHmacSha512KeyBytes() {
        var bytes = new byte[64];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }

    private record TestSymmetric(String signingKey) implements Symmetric {
        @Override public String getSigningKey() { return signingKey; }
        @Override public String getIssuerId() { return "test-issuer"; }
        @Override public long getSkewSeconds() { return Issuer.DEFAULT_MAX_SKEW_SECONDS; }
        @Override public URL getBaseURL() { return null; }
    }
}

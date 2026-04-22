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

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.ameba.oauth2.parser.HS512TokenParser;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * A DefaultTokenExtractorTest exercises the orchestration between JWT parsing,
 * issuer whitelisting and signature verification. The happy path uses the real
 * {@link HS512TokenParser} paired with a hand-rolled in-memory whitelist so that
 * the extractor is tested end-to-end without Mockito or network IO.
 *
 * @author Heiko Scherrer
 */
class DefaultTokenExtractorTest {

    private static final String ISSUER_ID = "test-issuer";

    @Test void canExtractAcceptsThreePartToken() {
        var testee = newExtractor("unused", randomKeyBase64());

        assertThat(testee.canExtract("a.b.c").isExtractionPossible()).isTrue();
    }

    @Test void canExtractRejectsTokensWithWrongNumberOfParts() {
        var testee = newExtractor(ISSUER_ID, randomKeyBase64());

        assertThat(testee.canExtract("a").isExtractionPossible()).isFalse();
        assertThat(testee.canExtract("a.b").isExtractionPossible()).isFalse();
        assertThat(testee.canExtract("a.b.c.d").isExtractionPossible()).isFalse();
    }

    @Test void extractRejectsTokenWithFewerThanTwoParts() {
        var testee = newExtractor(ISSUER_ID, randomKeyBase64());

        assertThatThrownBy(() -> testee.extract("only-one-part"))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Token is not a JWT");
    }

    @Test void extractReturnsJwtForValidSignedToken() {
        var keyBytes = randomKeyBytes();
        var base64Key = Encoders.BASE64.encode(keyBytes);
        var token = Jwts.builder()
                .subject("test-user")
                .issuer(ISSUER_ID)
                .expiration(oneHourFromNow())
                .signWith(Keys.hmacShaKeyFor(keyBytes))
                .compact();
        var testee = newExtractor(ISSUER_ID, base64Key);

        var result = testee.extract(token);

        assertThat(result.isExtractionPossible()).isTrue();
        assertThat(result.hasJwt()).isTrue();
    }

    @Test void extractRejectsTokenWithUnknownIssuer() {
        var keyBytes = randomKeyBytes();
        var base64Key = Encoders.BASE64.encode(keyBytes);
        var token = Jwts.builder()
                .subject("x")
                .issuer("some-other-issuer")
                .expiration(oneHourFromNow())
                .signWith(Keys.hmacShaKeyFor(keyBytes))
                .compact();
        var testee = newExtractor(ISSUER_ID, base64Key);

        assertThatThrownBy(() -> testee.extract(token))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Issuer not accepted");
    }

    @Test void extractRejectsTokenWithUnsupportedAlgorithm() {
        // Sign with HS384 but only register an HS512 parser — should fail with "Algorithm not supported".
        var keyBytes = new byte[48]; // HS384 min key size
        new SecureRandom().nextBytes(keyBytes);
        var base64Key = Encoders.BASE64.encode(keyBytes);
        var token = Jwts.builder()
                .subject("x")
                .issuer(ISSUER_ID)
                .expiration(oneHourFromNow())
                .signWith(Keys.hmacShaKeyFor(keyBytes), Jwts.SIG.HS384)
                .compact();
        var testee = newExtractor(ISSUER_ID, base64Key);

        assertThatThrownBy(() -> testee.extract(token))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Algorithm [HS384] not supported");
    }

    @Test void extractAcceptsTokenWithoutExpClaim() {
        // A JWT without exp is still accepted — some APIs issue non-expiring tokens deliberately.
        var keyBytes = randomKeyBytes();
        var base64Key = Encoders.BASE64.encode(keyBytes);
        var token = Jwts.builder()
                .subject("x")
                .issuer(ISSUER_ID)
                .signWith(Keys.hmacShaKeyFor(keyBytes))
                .compact();
        var testee = newExtractor(ISSUER_ID, base64Key);

        var result = testee.extract(token);

        assertThat(result.hasJwt()).isTrue();
    }

    @Test void extractRejectsTokenWithoutIssClaim() {
        var keyBytes = randomKeyBytes();
        var token = Jwts.builder()
                .subject("x")
                .expiration(oneHourFromNow())
                .signWith(Keys.hmacShaKeyFor(keyBytes))
                .compact();
        var testee = newExtractor(ISSUER_ID, Encoders.BASE64.encode(keyBytes));

        assertThatThrownBy(() -> testee.extract(token))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Missing iss claim");
    }

    private static DefaultTokenExtractor newExtractor(String issuerId, String base64SigningKey) {
        IssuerWhiteList<Issuer> whiteList = new IssuerWhiteList<>() {
            @Override
            public List<Issuer> getIssuers(String iss) {
                if (!issuerId.equals(iss)) {
                    return List.of();
                }
                return List.of(new TestSymmetric(issuerId, base64SigningKey));
            }

            @Override
            public Issuer getIssuer(String iss, String kid) {
                throw new UnsupportedOperationException("kid branch not exercised by these tests");
            }
        };
        return new DefaultTokenExtractor(whiteList, List.of(new HS512TokenParser()));
    }

    private static byte[] randomKeyBytes() {
        var bytes = new byte[64];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }

    private static String randomKeyBase64() {
        return Encoders.BASE64.encode(randomKeyBytes());
    }

    private static Date oneHourFromNow() {
        return new Date(System.currentTimeMillis() + 3_600_000L);
    }

    private record TestSymmetric(String issuerId, String signingKey) implements Symmetric {
        @Override public String getSigningKey() { return signingKey; }
        @Override public String getIssuerId() { return issuerId; }
        @Override public long getSkewSeconds() { return Issuer.DEFAULT_MAX_SKEW_SECONDS; }
        @Override public URL getBaseURL() { return null; }
    }
}

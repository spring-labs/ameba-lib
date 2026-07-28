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

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.SigningKeyNotFoundException;
import io.jsonwebtoken.Jwts;
import org.ameba.oauth2.Asymmetric;
import org.ameba.oauth2.InvalidTokenException;
import org.ameba.oauth2.Issuer;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * A RSA256TokenParserTest verifies the guards of the asymmetric RSA SHA-256 token parser,
 * its {@link org.ameba.oauth2.TokenParser#supportAlgorithm()} contract, and the full
 * sign/verify round-trip through a fake {@link JwkProvider}.
 *
 * @author Heiko Scherrer
 */
class RSA256TokenParserTest {

    private static final String KID = "test-kid";

    @Test void supportAlgorithmIsRS256() {
        var testee = new RSA256TokenParser();

        assertThat(testee.supportAlgorithm()).isEqualTo("RS256");
    }

    @Test void parseRejectsNullIssuer() {
        var testee = new RSA256TokenParser();

        assertThatThrownBy(() -> testee.parse("x.y.z", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("asymmetric issuer is null");
    }

    @Test void parseRejectsIssuerWithNullKid() {
        var testee = new RSA256TokenParser();
        var issuer = new TestAsymmetric(null);

        assertThatThrownBy(() -> testee.parse("x.y.z", issuer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("kid is null or empty");
    }

    @Test void parseRejectsIssuerWithEmptyKid() {
        var testee = new RSA256TokenParser();
        var issuer = new TestAsymmetric("");

        assertThatThrownBy(() -> testee.parse("x.y.z", issuer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("kid is null or empty");
    }

    @Test void parseReturnsJwsForValidSignedToken() throws Exception {
        var keyPair = newRsaKeyPair();
        var token = Jwts.builder()
                .subject("test-subject")
                .signWith(keyPair.getPrivate(), Jwts.SIG.RS256)
                .compact();
        var testee = new RSA256TokenParser(jwkProviderReturning(keyPair.getPublic()));

        var jws = testee.parse(token, new TestAsymmetric(KID));

        assertThat(jws).isNotNull();
        assertThat(jws.getPayload().getSubject()).isEqualTo("test-subject");
    }

    @Test void parseRejectsTokenSignedWithDifferentKeypair() throws Exception {
        var signingKeyPair = newRsaKeyPair();
        var verifyingKeyPair = newRsaKeyPair();
        var token = Jwts.builder()
                .subject("x")
                .signWith(signingKeyPair.getPrivate(), Jwts.SIG.RS256)
                .compact();
        var testee = new RSA256TokenParser(jwkProviderReturning(verifyingKeyPair.getPublic()));

        assertThatThrownBy(() -> testee.parse(token, new TestAsymmetric(KID)))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test void parseRejectsMalformedToken() throws Exception {
        var keyPair = newRsaKeyPair();
        var testee = new RSA256TokenParser(jwkProviderReturning(keyPair.getPublic()));

        assertThatThrownBy(() -> testee.parse("not.a.real.jwt", new TestAsymmetric(KID)))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test void parseWrapsJwkProviderLookupFailures() {
        JwkProvider failingProvider = kid -> { throw new SigningKeyNotFoundException("no key for " + kid, null); };
        var testee = new RSA256TokenParser(failingProvider);

        assertThatThrownBy(() -> testee.parse("x.y.z", new TestAsymmetric(KID)))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("no key");
    }

    private static KeyPair newRsaKeyPair() throws NoSuchAlgorithmException {
        var kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        return kpg.generateKeyPair();
    }

    private static JwkProvider jwkProviderReturning(PublicKey publicKey) {
        var jwk = new Jwk(KID, "RSA", "RS256", null, (List<String>) null, null, null, null, null) {
            @Override public PublicKey getPublicKey() { return publicKey; }
        };
        return kid -> {
            if (!KID.equals(kid)) {
                throw new SigningKeyNotFoundException("unknown kid " + kid, null);
            }
            return jwk;
        };
    }

    private record TestAsymmetric(String kid) implements Asymmetric {
        @Override public String getKID() { return kid; }
        @Override public URL getJWKURL() { return null; }
        @Override public String getIssuerId() { return "test-issuer"; }
        @Override public long getSkewSeconds() { return Issuer.DEFAULT_MAX_SKEW_SECONDS; }
        @Override public URL getBaseURL() { return null; }
    }
}

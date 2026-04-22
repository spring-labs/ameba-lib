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
package org.ameba.oauth2.issuer;

import org.ameba.oauth2.InvalidTokenException;
import org.ameba.oauth2.Issuer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;

import java.net.URI;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * A ConfigurationIssuerWhiteListTest pins the matching semantics of the property-driven
 * issuer whitelist (issuer-id and kid both must match) and the fast-fail behaviour of the
 * constructor when a malformed baseURL slips through Spring property binding.
 *
 * @author Heiko Scherrer
 */
class ConfigurationIssuerWhiteListTest {

    private static final String ISSUER_ID = "https://example.com/realms/test";
    private static final String BASE_URL = "https://example.com";
    private static final String SIGNING_KEY = "c2VjcmV0";
    private static final String KID = "key-1";
    private static final int CUSTOM_SKEW = 30;

    @Test void getIssuersReturnsSingletonForMatchingIssuerId() throws Exception {
        var testee = new ConfigurationIssuerWhiteList(ISSUER_ID, CUSTOM_SKEW, BASE_URL, SIGNING_KEY, jwksUrl(), KID);

        var issuers = testee.getIssuers(ISSUER_ID);

        assertThat(issuers).hasSize(1);
        var issuer = issuers.getFirst();
        assertThat(issuer).isInstanceOf(ConfiguredIssuer.class);
        assertThat(issuer.getIssuerId()).isEqualTo(ISSUER_ID);
        assertThat(issuer.getSkewSeconds()).isEqualTo(CUSTOM_SKEW);
    }

    @Test void getIssuersRejectsUnknownIssuerId() throws Exception {
        var testee = new ConfigurationIssuerWhiteList(ISSUER_ID, CUSTOM_SKEW, BASE_URL, SIGNING_KEY, jwksUrl(), KID);

        assertThatThrownBy(() -> testee.getIssuers("other-issuer"))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("issuer not accepted");
    }

    @Test void getIssuerReturnsConfiguredIssuerWithAllFields() throws Exception {
        var jwks = jwksUrl();
        var testee = new ConfigurationIssuerWhiteList(ISSUER_ID, CUSTOM_SKEW, BASE_URL, SIGNING_KEY, jwks, KID);

        var issuer = (ConfiguredIssuer) testee.getIssuer(ISSUER_ID, KID);

        assertThat(issuer.getIssuerId()).isEqualTo(ISSUER_ID);
        assertThat(issuer.getSkewSeconds()).isEqualTo(CUSTOM_SKEW);
        assertThat(issuer.getSigningKey()).isEqualTo(SIGNING_KEY);
        assertThat(issuer.getKID()).isEqualTo(KID);
        assertThat(issuer.getJWKURL()).isEqualTo(jwks);
        assertThat(issuer.getBaseURL().toString()).isEqualTo(BASE_URL);
    }

    @Test void getIssuerRejectsUnknownIssuerId() throws Exception {
        var testee = new ConfigurationIssuerWhiteList(ISSUER_ID, CUSTOM_SKEW, BASE_URL, SIGNING_KEY, jwksUrl(), KID);

        assertThatThrownBy(() -> testee.getIssuer("other-issuer", KID))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("issuer not accepted");
    }

    @Test void getIssuerRejectsKnownIssuerWithUnknownKid() throws Exception {
        var testee = new ConfigurationIssuerWhiteList(ISSUER_ID, CUSTOM_SKEW, BASE_URL, SIGNING_KEY, jwksUrl(), KID);

        assertThatThrownBy(() -> testee.getIssuer(ISSUER_ID, "other-kid"))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("kid not accepted");
    }

    @Test void shortConstructorDefaultsToIssuerDefaultSkew() throws Exception {
        var testee = new ConfigurationIssuerWhiteList(ISSUER_ID, BASE_URL, SIGNING_KEY, jwksUrl(), KID);

        var issuer = testee.getIssuer(ISSUER_ID, KID);

        assertThat(issuer.getSkewSeconds()).isEqualTo(Issuer.DEFAULT_MAX_SKEW_SECONDS);
    }

    @Test void constructorRejectsMalformedBaseUrl() throws Exception {
        assertThatThrownBy(() ->
                new ConfigurationIssuerWhiteList(ISSUER_ID, CUSTOM_SKEW, "not a url", SIGNING_KEY, jwksUrl(), KID))
                .isInstanceOf(InvalidConfigurationPropertyValueException.class)
                .hasMessageContaining("baseURL");
    }

    private static URL jwksUrl() throws Exception {
        return URI.create("https://example.com/.well-known/jwks.json").toURL();
    }
}

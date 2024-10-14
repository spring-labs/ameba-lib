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
package org.ameba.oauth2.issuer;

import org.ameba.oauth2.Asymmetric;
import org.ameba.oauth2.Issuer;
import org.ameba.oauth2.Symmetric;

import java.net.URL;

/**
 * A ConfiguredIssuer.
 *
 * @author Heiko Scherrer
 */
public class ConfiguredIssuer implements Symmetric, Asymmetric, Issuer {

    private String issuerId;
    private int skewSeconds;
    private URL baseURL;
    private String signingKey;
    private URL jwkURL;
    private String kid;

    public ConfiguredIssuer(String issuerId, int skewSeconds, URL baseURL, String signingKey, URL jwkURL, String kid) {
        this.issuerId = issuerId;
        this.skewSeconds = skewSeconds;
        this.baseURL = baseURL;
        this.signingKey = signingKey;
        this.jwkURL = jwkURL;
        this.kid = kid;
    }

    /**
     * The unique ID of the issuer, e.g. an URL (in case of ReadHat KeyCloak).
     *
     * @return As String
     */
    @Override
    public String getIssuerId() {
        return issuerId;
    }

    /**
     * Leap seconds allowed when a token signature is validated.
     *
     * @return In seconds
     */
    @Override
    public long getSkewSeconds() {
        return skewSeconds;
    }

    /**
     * Return the base URL of the authorization server.
     *
     * @return As URL
     */
    @Override
    public URL getBaseURL() {
        return baseURL;
    }

    /**
     * Return the KID in terms of JWK.
     *
     * @return As String
     */
    @Override
    public String getKID() {
        return kid;
    }

    /**
     * Return the URL where to resolve JWK information from.
     *
     * @return The JWK endpoint URL
     */
    @Override
    public URL getJWKURL() {
        return jwkURL;
    }

    /**
     * The secret signing key used to build and validate the signature.
     *
     * @return As String
     */
    @Override
    public String getSigningKey() {
        return signingKey;
    }
}

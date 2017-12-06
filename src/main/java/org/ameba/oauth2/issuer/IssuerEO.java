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
package org.ameba.oauth2.issuer;

import org.ameba.integration.jpa.ApplicationEntity;
import org.ameba.oauth2.Asymmetric;
import org.ameba.oauth2.Issuer;
import org.ameba.oauth2.Symmetric;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.net.URL;

/**
 * An IssuerEO is a persistent entity class to store information about Token Issuers.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
@Entity
@Table(name = "CORE_ISSUER")
public class IssuerEO extends ApplicationEntity implements Symmetric, Asymmetric, Issuer {

    /** The unique ID of the issuer, this may be an URL. */
    @Column(name = "C_ISS_URL", nullable = false, unique = true, length = 512)
    private URL issUrl;
    /** The .wellknown endpoint URL where to get the JWK cert from. */
    @Column(name = "C_JWK_URL", length = 512)
    private URL jwkUrl;
    /** The ID of the JWK key that is used for asymmetric signing algorithms. */
    @Column(name = "C_KID", length = 512)
    private String kid;
    /** The signing key that is used for symmetric signing algorithms. */
    @Column(name = "C_SIGN_KEY", length = 1024)
    private String signingKey;
    /**
     * A duration in seconds that the implementation allows as a time frame when tokens
     * are validated against an expiration date.
     */
    @Column(name = "C_TOKEN_LIFETIME")
    private long tokenLifetime;

    @Override
    public String getIssuerId() {
        return issUrl.toString();
    }

    @Override
    public String getSigningKey() {
        return signingKey;
    }

    @Override
    public long getSkewSeconds() {
        return tokenLifetime;
    }

    @Override
    public URL getBaseURL() {
        return issUrl;
    }

    @Override
    public String getKID() {
        return kid;
    }

    @Override
    public URL getJWKURL() {
        return jwkUrl;
    }
}

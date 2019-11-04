/*
 * Copyright 2014-2018 the original author or authors.
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

import static org.ameba.oauth2.issuer.IssuerEO.TABLE_NAME;

/**
 * An IssuerEO is a persistent entity class to store information about Token Issuers.
 *
 * @author Heiko Scherrer
 */
@Entity
@Table(name = TABLE_NAME)
public class IssuerEO extends ApplicationEntity implements Symmetric, Asymmetric, Issuer {

    public static final String TABLE_NAME = "T_ISSUER";
    /** An unique name of the issuer. */
    @Column(name = "C_NAME", nullable = false, unique = true)
    private String name;
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

    /**
     * Dear JPA...
     */
    IssuerEO() {
    }

    /**
     * Create an Issuer with the business key.
     *
     * @param name The name of the issuer
     * @param issUrl The base URL or issuer URL
     */
    public IssuerEO(String name, URL issUrl) {
        this.name = name;
        this.issUrl = issUrl;
    }

    /**
     * Return the name of the issuer.
     *
     * @return The name as String
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the issuer.
     *
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     *
     * @return The issuer URL as String
     */
    @Override
    public String getIssuerId() {
        return issUrl.toString();
    }

    /**
     * Get the issuer URL.
     *
     * @return Issuer URL
     */
    public URL getIssUrl() {
        return issUrl;
    }

    /**
     * Set the issuer URL.
     *
     * @param issUrl Issuer URL.
     */
    public void setIssUrl(URL issUrl) {
        this.issUrl = issUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSigningKey() {
        return signingKey;
    }

    /**
     * Set the signing key for symmetric encryption.
     *
     * @param signingKey The signing key.
     */
    public void setSigningKey(String signingKey) {
        this.signingKey = signingKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSkewSeconds() {
        return tokenLifetime;
    }

    /**
     * Set the skew seconds.
     *
     * @param skewSeconds Skew seconds
     */
    public void setSkwSeconds(long skewSeconds) {
        this.tokenLifetime = skewSeconds;
    }

    /**
     * {@inheritDoc}
     *
     * @return The issuer URL
     */
    @Override
    public URL getBaseURL() {
        return issUrl;
    }

    /**
     * Set the base URL.
     *
     * @param baseURL Base URL
     */
    public void setBaseURL(URL baseURL) {
        this.issUrl = baseURL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKID() {
        return kid;
    }

    /**
     * Set the key identifier (KID) to identify the key in JWK format.
     *
     * @param kid The KID
     */
    public void setKID(String kid) {
        this.kid = kid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL getJWKURL() {
        return jwkUrl;
    }

    /**
     * Set the JWK URL where to get the JWK information from.
     *
     * @param jwkUrl JWK URL.
     */
    public void setJWKURL(URL jwkUrl) {
        this.jwkUrl = jwkUrl;
    }
}

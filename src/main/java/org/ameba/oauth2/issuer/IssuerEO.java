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

    @Column(name = "C_URL", nullable = false, unique = true)
    private URL issUrl;
    @Column(name = "C_PUB_KEY", length = 2048)
    private String publicKey;
    @Column(name = "C_SIGN_KEY", length = 1024)
    private String signingKey;
    @Column(name = "C_TOKEN_LIFETIME")
    private long tokenLifetime;

    /** Dear JPA... */
    protected IssuerEO() {
    }

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
    public String getPublicKey() {
        return publicKey;
    }
}

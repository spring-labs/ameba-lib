/*
 * Copyright 2005-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */package org.ameba.oauth2.issuer;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.SigningKeyNotFoundException;
import com.auth0.jwk.UrlJwkProvider;
import org.ameba.exception.TechnicalRuntimeException;
import org.ameba.oauth2.Issuer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A JwksUrlRepository.
 *
 * @author Heiko Scherrer
 */
public class JwksUrlRepository implements IssuerRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwksUrlRepository.class);
    private final JpaIssuerRepository jpaIssuerRepository;

    public JwksUrlRepository(JpaIssuerRepository jpaIssuerRepository) {
        this.jpaIssuerRepository = jpaIssuerRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Issuer> findByIssUrl(URL issUrl) {
        return jpaIssuerRepository.findByIssUrl(issUrl).stream().map(eo ->  new Issuer() {
            @Override
            public String getIssuerId() {
                return eo.getIssuerId();
            }

            @Override
            public long getSkewSeconds() {
                return eo.getSkewSeconds();
            }

            @Override
            public URL getBaseURL() {
                return eo.getBaseURL();
            }
        }).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Issuer> findByIssUrlAndKid(URL issUrl, String kid) {
        if (issUrl == null || kid == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        var issuers = jpaIssuerRepository.findByIssUrl(issUrl);
        if (!issuers.isEmpty()) {
            Jwk jwk;
            try {
                IssuerEO result = null;
                jwk = new UrlJwkProvider(issuers.get(0).getJWKURL(), 60000, 60000).get(kid);
                for (var issuer : issuers) {
                    if (issuer.getKID().equals(jwk.getId())) {
                        result = issuer;
                        LOGGER.trace("Resolved Issuer with KID [{}]", result);
                        break;
                    }
                }
                if (result == null) {

                    // New Kids, save it!
                    result = saveNewIssuer(kid, issuers.get(0));
                }
                return Optional.of(result);
            } catch (SigningKeyNotFoundException sknfe) {

                // Kid has been removed - rolling kid
                return Optional.of(saveNewIssuer(kid, issuers.get(0)));
            } catch (Exception e) {
                throw new TechnicalRuntimeException(e.getMessage(), e);
            }
        }
        LOGGER.warn("Token issuer is not accepted");
        return Optional.empty();
    }

    private IssuerEO saveNewIssuer(String kid, IssuerEO issuer) {
        var newIssuer = new IssuerEO(UUID.randomUUID().toString(), issuer.getIssUrl());
        newIssuer.setKID(kid);
        newIssuer.setBaseURL(issuer.getBaseURL());
        newIssuer.setJWKURL(issuer.getJWKURL());
        newIssuer.setSkwSeconds(issuer.getSkewSeconds());
        LOGGER.debug("Saving new Issuer entry with new KID: [{}]", newIssuer);
        return jpaIssuerRepository.save(newIssuer);
    }
}

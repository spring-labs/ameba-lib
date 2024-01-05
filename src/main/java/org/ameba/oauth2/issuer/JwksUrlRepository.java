/*
 * Copyright 2005-2023 the original author or authors.
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
import com.auth0.jwk.UrlJwkProvider;
import org.ameba.exception.TechnicalRuntimeException;
import org.ameba.oauth2.Issuer;

import java.net.URL;
import java.util.Optional;

/**
 * A JwksUrlRepository.
 *
 * @author Heiko Scherrer
 */
public class JwksUrlRepository implements IssuerRepository {

    private final JpaIssuerRepository jpaIssuerRepository;

    public JwksUrlRepository(JpaIssuerRepository jpaIssuerRepository) {
        this.jpaIssuerRepository = jpaIssuerRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Issuer> findByIssUrl(URL issUrl) {
        return jpaIssuerRepository.findByIssUrl(issUrl).map(e -> e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Issuer> findByIssUrlAndKid(URL issUrl, String kid) {
        var issuerOpt = jpaIssuerRepository.findByIssUrl(issUrl);
        if (issuerOpt.isPresent()) {
            Jwk jwk;
            try {
                jwk = new UrlJwkProvider(issuerOpt.get().getJWKURL(), 60000, 60000).get(kid);
            } catch (Exception e) {
                throw new TechnicalRuntimeException(e.getMessage(), e);
            }
            var issuer = issuerOpt.get();
            if (!issuer.getKID().equals(jwk.getId())) {
                // think about saving a new issuer with this kid and a new generated name
            }
            issuer.setKID(jwk.getId());
            return Optional.of(issuer);
        }
        return Optional.empty();
    }
}

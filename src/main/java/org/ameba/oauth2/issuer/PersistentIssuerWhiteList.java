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

import org.ameba.oauth2.InvalidTokenException;
import org.ameba.oauth2.IssuerWhiteList;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * A PersistentIssuerWhiteList.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
public class PersistentIssuerWhiteList implements IssuerWhiteList {

    private final IssuerRepository repository;

    @Inject
    private PersistentIssuerWhiteList(IssuerRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<IssuerEO> getIssuer(String issuerId) {
        try {
            return repository.findByIssUrl(new URL(issuerId));
        } catch (MalformedURLException e) {
            throw new InvalidTokenException("Format of issuer not supported. The current implementation supports URL formats only");
        }
    }
}

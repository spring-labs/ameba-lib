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
package org.ameba.integration.hibernate;

import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * A DefaultMultiTenantConnectionProvider extends the abstract Hibernate type to provide the actual {@link ConnectionProvider}
 * from a cache. The implementation is taken untouched from the Hibernate reference documentation.
 *
 * @author <a href="mailto:hscherrer@interface21.io">Heiko Scherrer</a>
 */
public class DefaultMultiTenantConnectionProvider extends AbstractMultiTenantConnectionProvider {

    private final Map<String, ConnectionProvider> connectionProviderMap = new HashMap<>();

    /**
     * {@inheritDoc}
     *
     * Additionally feeding a simple {@link HashMap} with all providers.
     */
    public DefaultMultiTenantConnectionProvider(Map<String, ConnectionProvider> connectionProviderMap) {
        this.connectionProviderMap.putAll(connectionProviderMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ConnectionProvider getAnyConnectionProvider() {
        return connectionProviderMap.values().iterator().next();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ConnectionProvider selectConnectionProvider(String tenantIdentifier) {
        return connectionProviderMap.get(tenantIdentifier);
    }
}

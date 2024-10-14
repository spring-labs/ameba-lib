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
package org.ameba.integration.hibernate;

import org.ameba.tenancy.TenantHolder;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

/**
 * A TenantResolverTenancyStrategy is the adapter between the Hibernate {@link CurrentTenantIdentifierResolver}
 * entry point and the Ameba {@link TenantHolder} implementation.
 *
 * @author Heiko Scherrer
 */
public class TenantResolverTenancyStrategy implements CurrentTenantIdentifierResolver {

    private final String defaultDatabaseSchema;

    public TenantResolverTenancyStrategy(String defaultDatabaseSchema) {
        this.defaultDatabaseSchema = defaultDatabaseSchema;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String resolveCurrentTenantIdentifier() {
        return TenantHolder.getCurrentTenant() == null ? defaultDatabaseSchema : TenantHolder.getCurrentTenant();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}

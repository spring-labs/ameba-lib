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

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;

import java.util.Map;

/**
 * A SchemaSeparationConfigurator is a Spring Boot recognized class type to customize Hibernate properties
 * programmatically at application startup (when the EntityManagerFactory and SessionFactory are bootstrapped). In fact
 * this implementation configures Hibernate to use a schema-based data separation.
 * <p />
 * The tenant resolution strategy may change between projects and can be defined as an attribute of the {@code EnableMultiTenancy}
 * annotation.
 *
 * @author <a href="mailto:hscherrer@interface21.io">Heiko Scherrer</a>
 */
public class SchemaSeparationConfigurator implements HibernatePropertiesCustomizer {

    public static Object tenantResolver;

    /**
     * {@inheritDoc}
     * <p>
     * Setup schema-based data separation for multiple tenants.
     */
    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, DefaultMultiTenantConnectionProvider.class);
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
    }
}

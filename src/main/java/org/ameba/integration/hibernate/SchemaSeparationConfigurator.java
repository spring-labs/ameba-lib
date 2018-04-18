/*
 * openwms.org, the Open Warehouse Management System.
 * Copyright (C) 2014 Heiko Scherrer
 *
 * This file is part of openwms.org.
 *
 * openwms.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * openwms.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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

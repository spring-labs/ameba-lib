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

import org.ameba.annotation.ExcludeFromScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A SchemaBasedTenancyConfiguration is a Spring configuration file, excluded from component scanning to bootstrap beans
 * according to the multi-tenancy database separation based on the {@code SeparationStrategy.SCHEMA}.
 *
 * @author <a href="mailto:hscherrer@interface21.io">Heiko Scherrer</a>
 */
@ExcludeFromScan
@Configuration
class SchemaBasedTenancyConfiguration {

    /** Contribute Hibernate properties to use schema based separation. */
    @Bean HibernatePropertiesCustomizer schemaSeparationConfigurator() {
        return new SchemaSeparationConfigurator();
    }

    @Bean DefaultMultiTenantConnectionProvider DefaultMultiTenantConnectionProvider() {
        return new DefaultMultiTenantConnectionProvider();
    }
}

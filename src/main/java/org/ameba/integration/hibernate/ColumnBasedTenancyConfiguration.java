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

import org.ameba.annotation.ExcludeFromScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A ColumnBasedTenancyConfiguration is a Spring configuration file, excluded from component scanning to bootstrap beans according to the
 * multi-tenancy partitioning-based separation activated with the {@code SeparationStrategy.DISCRIMINATOR} attribute.
 *
 * @author Heiko Scherrer
 */
@ExcludeFromScan
@Configuration
public class ColumnBasedTenancyConfiguration {

    /** Contribute Hibernate properties to use partitioning-based separation. */
    @Bean HibernatePropertiesCustomizer schemaSeparationConfigurator() {
        return new ColumnSeparationConfigurator();
    }
}

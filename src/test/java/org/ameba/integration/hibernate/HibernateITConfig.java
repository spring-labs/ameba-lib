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

import org.ameba.integration.EnableMultiTenancy;
import org.ameba.integration.SeparationStrategy;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * A HibernateITConfig.
 *
 * @author Heiko Scherrer
 */
@Configuration
@EnableMultiTenancy(separationStrategy = SeparationStrategy.SCHEMA,
    defaultDatabaseSchema = "PUBLIC",
    tenantSchemaPrefix = "T_") // h2 is case-sensitive
@EnableJpaRepositories(basePackageClasses = HibernateITConfig.class)
@EntityScan(basePackageClasses = HibernateITConfig.class)
public class HibernateITConfig {

}

/*
 * Copyright 2015-2023 the original author or authors.
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

import org.ameba.app.BaseConfiguration;
import org.ameba.tenancy.TenantHolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A HibernateSchemaBasedTenancyConfigurationTest.
 *
 * @author Heiko Scherrer
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = false, properties = "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect")
@ContextConfiguration(classes = HibernateITConfig.class)
@ComponentScan(basePackageClasses = {HibernateSchemaBasedTenancyConfigurationTest.class, BaseConfiguration.class})
public class HibernateSchemaBasedTenancyConfigurationTest {

    @Autowired
    private TransactionalService service;

    @MockBean
    private Tracer tracer;

    @AfterAll
    public static void clearContext() {
        TenantHolder.destroy();
    }

    @Test void multipleSchemaTest() throws Exception {
        /*
        we need to wrap all operations into a transactional service, because the connection gets pooled again
        after the tx commits. Unfortunately a DataJpaTest itself is a transactional test, therefore we cannot takeover
        control of transaction boundaries.
        */
        // Let's create two empty schemas first
        service.createSchema("SCHEMA_1");
        service.createSchema("SCHEMA_2");

        // Switch Tenant and create record
        TenantHolder.setCurrentTenant("SCHEMA_1");
        service.create("SCHEMA_1");

        // Switch Tenant and create record
        TenantHolder.setCurrentTenant("SCHEMA_2");
        service.create("SCHEMA_2");

        // Switch Tenant and assert...
        TenantHolder.setCurrentTenant("SCHEMA_1");
        Optional<TestEntity> e1 = service.findBySchemaName("SCHEMA_1");
        Optional<TestEntity> e2 = service.findBySchemaName("SCHEMA_2");
        assertTrue(e1.isPresent());
        assertFalse(e2.isPresent());

        TenantHolder.setCurrentTenant("SCHEMA_2");
        e1 = service.findBySchemaName("SCHEMA_1");
        e2 = service.findBySchemaName("SCHEMA_2");
        assertFalse(e1.isPresent());
        assertTrue(e2.isPresent());
    }
}

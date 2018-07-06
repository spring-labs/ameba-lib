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

import org.ameba.app.BaseConfiguration;
import org.ameba.tenancy.TenantHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * A HibernateSchemaBasedTenancyConfigurationTest.
 *
 * @author <a href="mailto:hscherrer@interface21.io">Heiko Scherrer</a>
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = HibernateITConfig.class)
@ComponentScan(basePackageClasses = {HibernateSchemaBasedTenancyConfigurationTest.class, BaseConfiguration.class})
public class HibernateSchemaBasedTenancyConfigurationTest {

    @Autowired
    private TransactionalService service;

    public
    @Test
    void multipleSchemaTest() throws Exception {
        /*
        we need to wrap all operations into a transactional service, because the connection gets pooled again
        after the tx commits. Unfortunately a DataJpaTest itself is a transactional test, therefore we cannot takeover
        control of transaction boundaries.
        */
        // Lets create two empty schemas first
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

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

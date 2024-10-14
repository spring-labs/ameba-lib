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
package org.ameba.integration.jpa;

import org.ameba.app.BaseConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A BaseEntityTest.
 *
 * @author Heiko Scherrer
 */
@DataJpaTest(showSql = false)
@ContextConfiguration(classes = JPAITConfig.class)
@ComponentScan(basePackageClasses = {BaseEntityIT.class, BaseConfiguration.class})
class BaseEntityIT {

    @Autowired
    private TestEntityManager em;

    @Test void shall_setIsNew() {
        var testee = new TestEntity();
        assertThat(testee.isNew()).isTrue();
        testee = em.persist(testee);
        assertThat(testee.isNew()).isFalse();
    }

    @Test void shall_set_PK() {
        var testee = new TestEntity();
        assertThat(testee.getPk()).isNull();
        assertThat(em.persist(testee).getPk()).isNotNull();
    }

    @Test void shall_set_Version() {
        var testee = new TestEntity();
        assertThat(testee.getOl()).isZero();

        var entity = em.persist(testee);
        assertThat(entity.getOl()).isZero();

        entity.inc();
        entity = em.merge(entity);
        em.flush();
        assertThat(entity.getOl()).isEqualTo(1);
    }

    @Test void shall_set_createDt() {
        SecurityContextHolder.setContext(new SecurityContextImpl(
                new UsernamePasswordAuthenticationToken("user", "password"))
        );
        var testee = new TestEntity();
        assertThat(testee.getCreateDt()).isNull();
        assertThat(testee.getCreatedBy()).isNull();

        testee = em.persist(testee);

        assertThat(testee.getCreateDt()).isNotNull();
        assertThat(testee.getCreatedBy()).isEqualTo("user");
    }

    @Test void shall_set_lastModifiedDt() {
        SecurityContextHolder.setContext(new SecurityContextImpl(
                new UsernamePasswordAuthenticationToken("user", "password"))
        );
        var testee = new TestEntity();
        assertThat(testee.getLastModifiedDt()).isNull();
        assertThat(testee.getLastModifiedBy()).isNull();

        testee = em.persist(testee);

        assertThat(testee.getLastModifiedDt()).isNotNull();
        assertThat(testee.getLastModifiedBy()).isEqualTo("user");
    }
}
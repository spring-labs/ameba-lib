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
package org.ameba.integration.jpa;

import org.ameba.app.BaseConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A BaseEntityTest.
 *
 * @author Heiko Scherrer
 * @version 1.0
 * @since 1.6
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = false)
@ContextConfiguration(classes = JPAITConfig.class)
@ComponentScan(basePackageClasses = {BaseEntityTest.class, BaseConfiguration.class})
class BaseEntityTest {

    @Autowired
    private TestEntityManager em;

    @Test void testIsNew() throws Exception {
        TestEntity te = new TestEntity();
        assertThat(te.isNew()).isTrue();
        te = em.persist(te);
        assertThat(te.isNew()).isFalse();
    }

    @Test void testGetPk() throws Exception {
        assertThat(new TestEntity().getPk()).isNull();
        assertThat(em.persist(new TestEntity()).getPk()).isNotNull();
    }

    @Test void testGetOl() throws Exception {
        assertThat(new TestEntity().getOl()).isZero();
        TestEntity entity = em.persist(new TestEntity());
        assertThat(entity.getOl()).isZero();
        entity.inc();
        entity = em.merge(entity);
        em.flush();
        assertThat(entity.getOl()).isEqualTo(1);
    }

    @Test void testGetCreateDt() throws Exception {
        assertThat(new TestEntity().getCreateDt()).isNull();
        assertThat(em.persist(new TestEntity()).getCreateDt()).isNotNull();
    }

    @Test void testGetLastModifiedDt() throws Exception {
        assertThat(new TestEntity().getLastModifiedDt()).isNull();
        assertThat(em.persist(new TestEntity()).getLastModifiedDt()).isNotNull();
    }
}
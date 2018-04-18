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
package org.ameba.integration.jpa;

import org.ameba.app.BaseConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A BaseEntityTest.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.0
 * @since 1.6
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = JPAITConfig.class)
@ComponentScan(basePackageClasses = {BaseEntityTest.class, BaseConfiguration.class})
public class BaseEntityTest {

    @Autowired
    private TestEntityManager em;

    @Test
    public void testIsNew() throws Exception {
        TestEntity te = new TestEntity();
        assertThat(te.isNew()).isTrue();
        te = em.persist(te);
        assertThat(te.isNew()).isFalse();
    }

    @Test
    public void testGetPk() throws Exception {
        assertThat(new TestEntity().getPk()).isNull();
        assertThat(em.persist(new TestEntity()).getPk()).isNotNull();
    }

    @Test
    public void testGetOl() throws Exception {
        assertThat(new TestEntity().getOl()).isEqualTo(0);
        TestEntity entity = em.persist(new TestEntity());
        assertThat(entity.getOl()).isEqualTo(0);
        entity.inc();
        entity = em.merge(entity);
        em.flush();
        assertThat(entity.getOl()).isEqualTo(1);
    }

    @Test
    public void testGetCreateDt() throws Exception {
        assertThat(new TestEntity().getCreateDt()).isNull();
        assertThat(em.persist(new TestEntity()).getCreateDt()).isNotNull();
    }

    @Test
    public void testGetLastModifiedDt() throws Exception {
        assertThat(new TestEntity().getLastModifiedDt()).isNull();
        assertThat(em.persist(new TestEntity()).getLastModifiedDt()).isNotNull();
    }
}
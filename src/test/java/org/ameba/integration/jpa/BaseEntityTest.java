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

import static org.assertj.core.api.Assertions.assertThat;

import org.ameba.IntegrationTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * A BaseEntityTest.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.0
 * @since 1.6
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = IntegrationTestConfig.class)
@ComponentScan(basePackageClasses = BaseEntityTest.class)
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

    }

    @Test
    public void testGetCreateDt() throws Exception {

    }

    @Test
    public void testGetLastModifiedDt() throws Exception {

    }
}
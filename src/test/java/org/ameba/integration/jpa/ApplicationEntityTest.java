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
 * A ApplicationEntityTest.
 *
 * @author Heiko Scherrer
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = false)
@ContextConfiguration(classes = JPAITConfig.class)
@ComponentScan(basePackageClasses = BaseEntityTest.class)
class ApplicationEntityTest {

    @Autowired
    private TestEntityManager em;

    @Test void testID() throws Exception {
        TestEntity te = new TestEntity();
        assertThat(te.getPersistentKey()).isNull();
        te = em.persist(te);
        assertThat(te.getPersistentKey()).isNotNull();
    }

    @Test void testNonUnique() throws Exception {
        TestEntity te1 = new TestApplicationEntity();
        TestEntity te2 = new TestApplicationEntity();
        te1 = em.persist(te1);
        te2 = em.persist(te2);
        assertThat(te1.getPersistentKey()).isEqualTo(te2.getPersistentKey());
    }
}

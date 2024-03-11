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

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;

/**
 * A ApplicationEntityTest.
 *
 * @author Heiko Scherrer
 */
@DataJpaTest(showSql = false)
@ContextConfiguration(classes = JPAITConfig.class)
@ComponentScan(basePackageClasses = BaseEntityTest.class)
class ApplicationEntityIT {

    @Autowired
    private TestEntityManager em;

    @Test void shall_set_persistentKey() {
        var testee = new TestApplicationEntity();
        assertThat(testee.getPersistentKey()).isNull();

        testee = em.persist(testee);

        assertThat(testee.hasPersistentKey()).isTrue();
        assertThat(testee.getPersistentKey()).isNotNull();
    }

    @Test void shall_fail_without_persistentKey() {
        var testee = new TestFailApplicationEntity();
        assertThat(testee.getPersistentKey()).isNull();
        try {
            em.persist(testee);
            em.flush();
            fail("Expecting a ConstraintViolationException");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(ConstraintViolationException.class);
        }
    }

    @Test void shall_create_unique_pKey() {
        var testee1 = new TestApplicationEntity();
        var testee2 = new TestApplicationEntity();

        testee1 = em.persist(testee1);
        testee2 = em.persist(testee2);

        assertThat(testee1.getPersistentKey()).isEqualTo(testee2.getPersistentKey());
    }
}

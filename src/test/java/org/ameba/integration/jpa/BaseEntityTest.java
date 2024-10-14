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

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * A BaseEntityTest.
 *
 * @author Heiko Scherrer
 */
class BaseEntityTest {

    @Test void testCreation() {
        var testee = new TestEntity();

        assertThat(testee.getPk()).isNull();
        assertThat(testee.getOl()).isZero();
        assertThat(testee.getCreatedBy()).isNull();
        assertThat(testee.getCreateDt()).isNull();
        assertThat(testee.getLastModifiedBy()).isNull();
        assertThat(testee.getLastModifiedDt()).isNull();

        testee.setOl(1);
        testee.setCreatedBy("SYSTEM");
        testee.setLastModifiedBy("SYSTEM");

        assertThat(testee.getOl()).isEqualTo(1);
        assertThat(testee.getCreatedBy()).isEqualTo("SYSTEM");
        assertThat(testee.getLastModifiedBy()).isEqualTo("SYSTEM");
    }

    @Test void testSetCreateDt() {
        var testee = new TestEntity();
        var now = LocalDateTime.now();

        assertThat(testee.getCreateDt()).isNull();
        testee.setCreateDt(now);
        assertThat(testee.getCreateDt()).isEqualTo(now);

        assertThatThrownBy(() -> testee.setCreateDt(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test void testSettLastModifiedDt() {
        var testee = new TestEntity();
        var now = LocalDateTime.now();

        assertThat(testee.getLastModifiedDt()).isNull();
        testee.setLastModifiedDt(now);
        assertThat(testee.getLastModifiedDt()).isEqualTo(now);

        assertThatThrownBy(() -> testee.setLastModifiedDt(null)).isInstanceOf(IllegalArgumentException.class);
    }
}
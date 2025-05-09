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

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;

/**
 * A TestApplicationEntity.
 *
 * @author Heiko Scherrer
 * @version 1.0
 * @since 1.6
 */
@Entity
class TestApplicationEntity extends ApplicationEntity {

    @Basic
    private int i = 0;

    public TestApplicationEntity() {
    }

    public void inc() {
        i++;
    }

    /**
     * JPA lifecycle method to set a random UUID before insertion.
     */
    @Override
    protected void onPersist() {
        setPersistentKey("1");
    }
}

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
 * A TestFailApplicationEntity.
 *
 * @author Heiko Scherrer
 */
@Entity
class TestFailApplicationEntity extends ApplicationEntity {

    @Basic
    private int i = 0;

    public TestFailApplicationEntity() {
    }

    @Override
    protected void onEntityPersist() {
        super.setPersistentKey(null);
    }

    public void inc() {
        i++;
    }
}

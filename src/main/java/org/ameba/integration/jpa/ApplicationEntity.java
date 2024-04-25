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

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * An ApplicationEntity adds a secondary key column that is an application assigned key that remains the same after database migrations. An
 * unique not-null constraint is placed on the column.
 *
 * @author Heiko Scherrer
 */
@MappedSuperclass
public class ApplicationEntity extends BaseEntity implements Serializable {

    public static final String C_ID = "C_PID";
    /**
     * Technical persisted key field, independently of the underlying database, assigned by the application layer, remains the same over
     * database migrations.
     */
    @Column(name = ApplicationEntity.C_ID, nullable = false, unique = true)
    private String pKey;

    /**
     * Get the persistent key.
     *
     * @return The persistent key
     */
    public String getPersistentKey() {
        return pKey;
    }

    /**
     * Check whether the persistent key is set.
     *
     * @return {@literal true} if the persistent key is set, otherwise {@literal false}
     */
    public boolean hasPersistentKey() {
        return pKey != null && !pKey.isEmpty();
    }

    /**
     * Set the persistent key from a subclass.
     *
     * @param pKey The persistent key to set
     */
    protected void setPersistentKey(String pKey) {
        this.pKey = pKey;
    }

    /** JPA lifecycle method sets a random UUID before insertion. */
    @PrePersist
    protected void onPersist() {
        if (this.pKey == null) {
            this.pKey = UUID.randomUUID().toString();
        }
        onEntityPersist();
    }

    /**
     * This method is called during the JPA entity persist operation {@link ApplicationEntity#onPersist()}.
     * It is intended to be overridden by subclasses of {@link ApplicationEntity} to implement
     * custom logic to be executed when the entity is persisted.
     *
     * Note that the method is marked as protected, which means that it is only accessible within
     * the class and subclasses. The default implementation of this method does nothing.
     *
     * @see ApplicationEntity#onPersist()
     */
    protected void onEntityPersist() {
        // Explicitly kept empty to be overridden by subclasses
    }

    /**
     * {@inheritDoc}
     *
     * Use the pKey for comparison.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (ApplicationEntity) o;
        return Objects.equals(pKey, that.pKey);
    }

    /**
     * {@inheritDoc}
     *
     * Use the pKey for calculation.
     */
    @Override
    public int hashCode() {
        return Objects.hash(pKey);
    }
}

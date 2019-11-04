/*
 * Copyright 2014-2018 the original author or authors.
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

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.UUID;

/**
 * An ApplicationEntity adds a secondary key column that is an application assigned key that remains the same after database migrations. An
 * unique not-null constraint is placed on the column.
 *
 * @author Heiko Scherrer
 * @version 1.0
 * @since 1.6
 */
@MappedSuperclass
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {ApplicationEntity.C_ID})})
public class ApplicationEntity extends BaseEntity {

    public static final String C_ID = "C_PID";
    /**
     * Technical or persisted key field, independent from the underlying database, assigned from application layer, remains the same over
     * database migrations. An unique constraint or limitation to not-null is explicitly not defined here, because it is a matter of
     * subclasses to defines those, if required.
     */
    @Column(name = ApplicationEntity.C_ID, nullable = false)
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
     * Set the persistent key from a subclass.
     *
     * @param pKey The persistent key to set
     */
    protected void setPersistentKey(String pKey) {
        this.pKey = pKey;
    }

    /**
     * JPA lifecycle method sets a random UUID before insertion.
     */
    @PrePersist
    protected void onPersist() {
        this.pKey = UUID.randomUUID().toString();
    }
}

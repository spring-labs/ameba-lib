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

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.util.Date;

import org.ameba.integration.TypedEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * A BaseEntity is a base superclass for JPA entities that comes with a mandatory primary key field, an application assign id and an
 * optimistic locking field.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.7
 * @since 1.4
 */
@MappedSuperclass
public class BaseEntity implements TypedEntity<Long> {

    /** Primary key, assigned by the underlying database or persistence strategy, shouldn't be used on API level. */
    @Id
    @Column(name = "C_PK")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    /** Optimistic locking field (Property name {@code version} might be used differently, hence lets call it {@code ol}). */
    @Version
    @Column(name = "C_OL")
    private long ol;

    /** Timestamp when the database record was inserted. */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "C_CREATED")
    @CreatedDate
    private Date createDt;

    /** Timestamp when the database record was updated the last time. */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "C_UPDATED")
    @LastModifiedDate
    private Date lastModifiedDt;

    /** Dear JPA ... */
    protected BaseEntity() {
    }

    /**
     * Checks whether this entity is a transient one or not.
     *
     * @return {@literal true} if transient, {@literal false} if detached or managed but not transient
     */
    public boolean isNew() {
        return pk != null;
    }

    /**
     * Get the primary key value.
     *
     * @return The primary key, may be {@literal null} for transient entities.
     */
    public Long getPk() {
        return pk;
    }

    /**
     * Get the current optimistic locking version number.
     *
     * @return The number of the optimistic locking field
     */
    protected long getOl() {
        return ol;
    }

    /**
     * Get the date when the entity was persisted.
     *
     * @return creation date
     */
    public Date getCreateDt() {
        return createDt;
    }

    /**
     * Get the date when the entity was modified.
     *
     * @return last modified date
     */
    public Date getLastModifiedDt() {
        return lastModifiedDt;
    }
}

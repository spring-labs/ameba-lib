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

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import org.ameba.integration.TypedEntity;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A BaseEntity is a base superclass for JPA entities that comes with a mandatory primary key field, an optimistic locking field and
 * properties for auditing purpose. The id property refers to one single generator of name {@literal generator}.
 *
 * @author Heiko Scherrer
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements TypedEntity<Long>, Serializable {

    /** Primary key, assigned by the database or persistence strategy, shouldn't be used nor exposed in public API. */
    @Id
    @Column(name = "C_PK")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generator")
    private Long pk;

    /** Optimistic locking field (property name {@code version} might be used differently, hence this is named {@code ol}). */
    @Version
    @Column(name = "C_OL")
    private long ol;

    /** Timestamp when the database record was inserted. */
    @Column(name = "C_CREATED")
    @CreatedDate
    private LocalDateTime createDt;

    /** Username who created the entity. */
    @Column(name = "C_CREATED_BY")
    @CreatedBy
    private String createdBy;

    /** Timestamp when the database record was updated the last time. */
    @Column(name = "C_UPDATED")
    @LastModifiedDate
    private LocalDateTime lastModifiedDt;

    /** Username who modified the entity. */
    @Column(name = "C_UPDATED_BY")
    @LastModifiedBy
    private String lastModifiedBy;

    /** Dear JPA ... */
    protected BaseEntity() {
    }

    /**
     * Checks whether this entity is a transient instance.
     *
     * @return {@literal true} if transient, {@literal false} if detached or managed but not transient
     */
    public boolean isNew() {
        return pk == null;
    }

    /**
     * Get the primary key value.
     *
     * @return The primary key, might be {@literal null} for transient entities.
     */
    public Long getPk() {
        return pk;
    }

    /**
     * Get the current optimistic locking version number.
     *
     * @return The number of the optimistic locking field
     */
    public long getOl() {
        return ol;
    }

    /**
     * Set the optimistic locking field.
     * <p>
     * <strong>CAUTION: This method is meant to be called by framework code only, like JPA ORM or mapping libraries, but not from
     * application code.</strong>
     * </p>
     *
     * @param ol The optimistic locking field
     */
    protected void setOl(long ol) {
        this.ol = ol;
    }

    /**
     * Get the date when the entity was persisted.
     *
     * @return creation date
     */
    public LocalDateTime getCreateDt() {
        return createDt;
    }

    /**
     * Set the date when the entity was persisted.
     *
     * @param createDt Creation date - not {@literal null}
     * @throws IllegalArgumentException if the given {@code createDt} is {@literal null}
     */
    protected void setCreateDt(LocalDateTime createDt) {
        if (createDt == null) {
            throw new IllegalArgumentException("CreateDt must not be null");
        }
        this.createDt = createDt;
    }

    /**
     * Get the user who created this entity.
     *
     * @return The username
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Set who has created the entity.
     *
     * @param createdBy The username
     */
    protected void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Get the date when the entity was modified.
     *
     * @return last modified date
     */
    public LocalDateTime getLastModifiedDt() {
        return lastModifiedDt;
    }

    /**
     * Set the date when the entity was modified.
     *
     * @param lastModifiedDt When the entity was modified the last time - not {@literal null}
     * @throws IllegalArgumentException if the given {@code createDt} is {@literal null}
     */
    protected void setLastModifiedDt(LocalDateTime lastModifiedDt) {
        if (lastModifiedDt == null) {
            throw new IllegalArgumentException("LastModifiedDt must not be null");
        }
        this.lastModifiedDt = lastModifiedDt;
    }

    /**
     * Get the user who modified this entity the last time.
     *
     * @return The users name
     */
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * Set who created the entity.
     *
     * @param lastModifiedBy The username
     */
    protected void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}

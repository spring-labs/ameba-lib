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
package org.ameba.integration.mongodb;

import java.util.Date;

import org.ameba.integration.TypedEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A BaseEntity is a base superclass for MongoDB document entities that defined useful fields for optimistic locking, unique persisted key,
 * and timestamps for last change or insertion date. To active the latter feature the Spring application context need to enable Spring Data
 * MongoDB MongoAuditingRegistrar. One approach to do this is to add {@code @EnableMongoAuditing} on a Spring configuration class.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.0
 * @since 1.6
 */
@Document
public class BaseEntity implements TypedEntity<String> {

    /** Unique persistent key, assigned by Spring Data MongoDB or MongoDB itself. */
    @Id
    @Field(FIELD_PK)
    private String pk;
    /** Field name to keep track of the current persisted identifier. */
    public static final String FIELD_PK = "_pk";

    /** Optimistic locking field, managed by Spring Data MongoDB. */
    @Version
    @Field(FIELD_OL_VERSION)
    private long ol;
    /** Field name of the optimistic locking field. */
    public static final String FIELD_OL_VERSION = "_ol";

    /** Timestamp when the document was inserted. */
    @Field(FIELD_CREATED)
    @CreatedDate
    private Date createDt;
    /** Field name of the timestamp when the document was inserted. */
    public static final String FIELD_CREATED = "_created";

    /** Timestamp when the document was updated the last time. */
    @Field(FIELD_UPDATED)
    @LastModifiedDate
    private Date lastModifiedDt;
    /** Field name of the timestamp when the document was updated the last time. */
    public static final String FIELD_UPDATED = "_updated";

    /**
     * Checks whether this entity is a transient one or not.
     *
     * @return {@literal true} if transient, {@literal false} if detached or managed but not transient
     */
    public boolean isNew() {
        return pk == null;
    }

    /**
     * Get the persistent key value.
     *
     * @return The persistent key, may be {@literal null} for transient entities.
     */
    public String getPk() {
        return pk;
    }

    /**
     * Used for serialization purpose.
     *
     * @param pk The persistent key to set
     */
    void setPk(String pk) {
        this.pk = pk;
    }

    /**
     * Get the timestamp this document has been persisted the first time.
     *
     * @return Creation date
     */
    public Date getCreateDt() {
        return createDt;
    }

    /**
     * Get the timestamp this document has been modified the last time.
     *
     * @return Last modified date
     */
    public Date getLastModifiedDt() {
        return lastModifiedDt;
    }
}

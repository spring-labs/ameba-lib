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
package org.ameba.oauth2.tenant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.ameba.integration.jpa.ApplicationEntity;

/**
 * A TenantEO is the persistent representation of a Tenant in a multi-tenancy system. It
 * is implemented as JPA entity class and is mapped onto a table with name {@value TABLE_NAME}.
 *
 * @author Heiko Scherrer
 */
@Entity
@Table(name = TenantEO.TABLE_NAME)
public class TenantEO extends ApplicationEntity {

    public static final String TABLE_NAME = "T_TENANT";
    /** An identifying opaque String. */
    @Column(name ="C_HASH", unique = true, nullable = false)
    private String hash;
    /** A security realm this tenant is assigned to. */
    @Column(name ="C_REALM")
    private String realm;
    /** The tenants name. */
    @Column(name ="C_NAME")
    private String name;

    /** Dear JPA... */
    protected TenantEO() {
    }

    /**
     * Create a tenant with its business key.
     *
     * @param hash The unique opaque String
     */
    public TenantEO(String hash) {
        this.hash = hash;
    }

    /**
     * Get the security realm.
     *
     * @return Realm
     */
    public String getRealm() {
        return realm;
    }

    /**
     * Set the security realm.
     *
     * @param realm The realm
     */
    public void setRealm(String realm) {
        this.realm = realm;
    }

    /**
     * Get the unique identifying opaque String.
     *
     * @return The hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * Set the unique identifying opaque String.
     *
     * @param hash The hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Get the tenants name.
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the tenants name.
     *
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Checks whether the tenant is assigned to the given realm.
     *
     * @param realm The realm to check
     * @return {@literal true} is so, otherwise {@literal false}
     */
    public boolean sameRealm(String realm) {
        return this.realm != null && this.realm.equals(realm);
    }
}

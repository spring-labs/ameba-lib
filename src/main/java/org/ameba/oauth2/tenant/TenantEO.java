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
package org.ameba.oauth2.tenant;

import org.ameba.integration.jpa.ApplicationEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A TenantEO.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
@Entity
@Table(name = "CORE_TENANT")
public class TenantEO extends ApplicationEntity {

    @Column(name ="C_HASH")
    private String hash;
    @Column(name ="C_REALM")
    private String realm;
    @Column(name ="C_NAME")
    private String name;

    public TenantEO() {
    }

    public String getRealm() {
        return realm;
    }

    public String getHash() {
        return hash;
    }

    public String getName() {
        return name;
    }

    public boolean sameRealm(String realm) {
        return this.realm != null && this.realm.equals(realm);
    }
}

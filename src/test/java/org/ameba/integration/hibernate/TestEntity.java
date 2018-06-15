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
package org.ameba.integration.hibernate;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * A TestEntity.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
@Entity
@NamedQueries({@NamedQuery(name="sn", query = "select t from TestEntity t where t.schema = :sn")})
public class TestEntity {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @Basic
    private String schema;

    TestEntity() {
    }

    TestEntity(String schema) {
        this.schema = schema;
    }

    public Long getId() {
        return id;
    }

    public String getSchema() {
        return schema;
    }
}

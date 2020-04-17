/*
 * Copyright 2015-2020 the original author or authors.
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

/**
 * A TransactionalServiceImpl is a helper class, transactional, managed by Spring to ensure transactions around public
 * service methods and that connections are put back into the pool, with tx commit/rollback.
 *
 * @author <a href="mailto:hscherrer@interface21.io">Heiko Scherrer</a>
 */
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Service
class TransactionalServiceImpl implements TransactionalService {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void create(String schemaName) {
        entityManager.merge(new TestEntity(schemaName));
    }

    @Override
    public void createSchema(String schemaName) {
        entityManager.createNativeQuery("CREATE SCHEMA "+schemaName+" AUTHORIZATION SA");
    }

    @Override
    public Optional<TestEntity> findBySchemaName(String schemaName) {
        try {
            return Optional.of(entityManager.createNamedQuery("sn", TestEntity.class).setParameter("sn", schemaName).getSingleResult());
        } catch (NoResultException nre) {
            return Optional.empty();
        }
    }
}

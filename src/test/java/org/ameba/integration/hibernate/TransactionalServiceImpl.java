/*
 * openwms.org, the Open Warehouse Management System.
 * Copyright (C) 2014 Heiko Scherrer
 *
 * This file is part of openwms.org.
 *
 * openwms.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * openwms.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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

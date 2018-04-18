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

import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

/**
 * A DefaultSessionFactoryStore.
 *
 * @author <a href="mailto:hscherrer@interface21.io">Heiko Scherrer</a>
 */
public class DefaultSessionFactoryStore implements Integrator {

    SessionFactoryImplementor sessionFactory;

    /**
     * Perform integration.
     *
     * @param metadata The "compiled" representation of the mapping information
     * @param sessionFactory The session factory being created
     * @param serviceRegistry The session factory's service registry
     */
    @Override
    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
        this.sessionFactory = sessionFactory;
        DefaultMultiTenantConnectionProvider service = serviceRegistry.getService(DefaultMultiTenantConnectionProvider.class);
        service.sessionFactory = sessionFactory;
    }

    /**
     * Tongue-in-cheek name for a shutdown callback.
     *
     * @param sessionFactory The session factory being closed.
     * @param serviceRegistry That session factory's service registry
     */
    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {

    }
}

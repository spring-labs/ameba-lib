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
import org.hibernate.service.UnknownServiceException;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A SessionFactoryIntegrator is a Hibernate {@link Integrator} that injects an already configured and built
 * SessionFactory into the {@link DefaultMultiTenantConnectionProvider}. By this mechanism the solely managed
 * {@link DefaultMultiTenantConnectionProvider} (that implements {@code MultiTenantConnectionProvider}) is able
 * to access the SessionFactory and finally the Connection.
 *
 * @author <a href="mailto:hscherrer@interface21.io">Heiko Scherrer</a>
 */
public class SessionFactoryIntegrator implements Integrator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionFactoryIntegrator.class);

    /**
     * {@inheritDoc}
     *
     * Set the {@code sessionFactory} at the {@link DefaultMultiTenantConnectionProvider}.
     */
    @Override
    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
        try {
            DefaultMultiTenantConnectionProvider service = serviceRegistry.getService(DefaultMultiTenantConnectionProvider.class);
            service.sessionFactory = sessionFactory;
        } catch (UnknownServiceException use) {
            LOGGER.error("No Hibernate service DefaultMultiTenantConnectionProvider is registered, check the bootstrap configuration (i.e. Spring configuration)");
        }
    }

    /**
     * {@inheritDoc}
     *
     * NOP
     */
    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {

    }
}

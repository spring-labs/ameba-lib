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

import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.UnknownServiceException;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A SessionFactoryIntegrator is a Hibernate {@link Integrator} that injects an already configured and built
 * SessionFactory into the {@link DefaultMultiTenantConnectionProvider}. By this mechanism the managed
 * {@link DefaultMultiTenantConnectionProvider} (that implements {@code MultiTenantConnectionProvider}) is able
 * to access the SessionFactory and finally create a JDBC Connection. Unfortunately Hibernate is not able to combine the
 * {@link Integrator} instance and a {@code MultiTenantConnectionProvider} instance in one class, therefore we need this
 * configuration mechanism.
 *
 * @author Heiko Scherrer
 */
public class SessionFactoryIntegrator implements Integrator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionFactoryIntegrator.class);

    /**
     * {@inheritDoc}
     * <p>
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
     * <p>
     * NOP
     */
    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {

    }
}

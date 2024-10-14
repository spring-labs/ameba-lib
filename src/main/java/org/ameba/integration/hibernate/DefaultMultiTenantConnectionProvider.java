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
package org.ameba.integration.hibernate;

import org.ameba.tenancy.TenantSchemaUtils;
import org.hibernate.HibernateException;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.engine.jndi.spi.JndiService;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate5.SessionFactoryUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A DefaultMultiTenantConnectionProvider extends the abstract Hibernate type to provide the actual {@link ConnectionProvider}
 * from a cache. The implementation is taken untouched from the Hibernate reference documentation.
 *
 * @author Heiko Scherrer
 */
public class DefaultMultiTenantConnectionProvider implements MultiTenantConnectionProvider<String>,
        ServiceRegistryAwareService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMultiTenantConnectionProvider.class);
    SessionFactoryImplementor sessionFactory;
    public static String defaultSchema;
    public static String tenantSchemaPrefix;

    private DataSource defaultDataSource;

    @Override
    public void injectServices(ServiceRegistryImplementor serviceRegistry) {
        Object dataSourceConfigValue = serviceRegistry.getService(ConfigurationService.class)
                .getSettings()
                .get(AvailableSettings.DATASOURCE);
        if (dataSourceConfigValue instanceof DataSource dataSource) {
            this.defaultDataSource = dataSource;
        } else if (dataSourceConfigValue instanceof String jndiName) {
            JndiService jndiService = serviceRegistry.getService(JndiService.class);
            if (jndiService == null) {
                throw new HibernateException("Could not locate JndiService from " + this.getClass().getName());
            }

            Object namedObject = jndiService.locate(jndiName);
            if (namedObject == null) {
                throw new HibernateException("JNDI name [" + jndiName + "] could not be resolved");
            }

            if (namedObject instanceof DataSource dataSource) {
                this.defaultDataSource = dataSource;
            } else {
                throw new HibernateException("Unknown object type [" +
                        namedObject.getClass().getName() +
                        "] found in JNDI location [" +
                        jndiName +
                        "]");
            }
        } else {
            throw new HibernateException("DataSource configuration not found");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getAnyConnection() throws SQLException {
        DataSource dataSource;
        if (sessionFactory == null) {
            LOGGER.warn("SessionFactory not set, using default DataSource");
            dataSource = this.defaultDataSource;
        } else {
            dataSource = SessionFactoryUtils.getDataSource(sessionFactory);
        }
        if (dataSource == null) {
            throw new SQLException("Cannot obtain JDBC Connection, no DataSource accessible from SessionFactory");
        }
        return dataSource.getConnection();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Notice that the JDBC driver must support setting the schema on the connection. Most drivers allow this, but
     * SQLServer does not allow to do so.
     */
    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = this.getAnyConnection();
        return TenantSchemaUtils.applyTenantSchema(connection, tenantIdentifier, tenantSchemaPrefix, defaultSchema);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        connection.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }
}

/*
 * Copyright 2015-2023 the original author or authors.
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

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.engine.spi.SessionFactoryImplementor;
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
public class DefaultMultiTenantConnectionProvider implements MultiTenantConnectionProvider<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMultiTenantConnectionProvider.class);
    SessionFactoryImplementor sessionFactory;
    public static String defaultSchema;

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getAnyConnection() throws SQLException {
        if (sessionFactory == null) {
            throw new SQLException("Cannot obtain JDBC Connection, no SessionFactoryImplementor set");
        }
        DataSource dataSource = SessionFactoryUtils.getDataSource(sessionFactory);
        if (dataSource== null) {
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
     *
     * Notice that the JDBC driver must support setting the schema on the connection. Most drivers allow this, but
     * SQLServer does not allow to do so.
     */
    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = this.getAnyConnection();
        if (!defaultSchema.equals(tenantIdentifier)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Switch to schema [{}]", tenantIdentifier);
            }
            connection.setSchema(tenantIdentifier);
        }
        return connection;
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

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

import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A SchemaSeparationConnectionProvider.
 *
 * @author <a href="mailto:hscherrer@interface21.io">Heiko Scherrer</a>
 */
class SchemaSeparationConnectionProvider implements ConnectionProvider {
    /**
     * Obtains a connection for Hibernate use according to the underlying strategy of this provider.
     *
     * @return The obtained JDBC connection
     * @throws SQLException Indicates a problem opening a connection
     * @throws HibernateException Indicates a problem otherwise obtaining a connection.
     */
    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }

    /**
     * Release a connection from Hibernate use.
     *
     * @param conn The JDBC connection to release
     * @throws SQLException Indicates a problem closing the connection
     * @throws HibernateException Indicates a problem otherwise releasing a connection.
     */
    @Override
    public void closeConnection(Connection conn) throws SQLException {
        conn.close();
    }

    /**
     * Does this connection provider support aggressive release of JDBC
     * connections and re-acquisition of those connections (if need be) later?
     * <p/>
     * This is used in conjunction with {@link Environment#RELEASE_CONNECTIONS}
     * to aggressively release JDBC connections.  However, the configured ConnectionProvider
     * must support re-acquisition of the same underlying connection for that semantic to work.
     * <p/>
     * Typically, this is only true in managed environments where a container
     * tracks connections by transaction or thread.
     * <p>
     * Note that JTA semantic depends on the fact that the underlying connection provider does
     * support aggressive release.
     *
     * @return {@code true} if aggressive releasing is supported; {@code false} otherwise.
     */
    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    /**
     * Can this wrapped service be unwrapped as the indicated type?
     *
     * @param unwrapType The type to check.
     * @return True/false.
     */
    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }

    /**
     * Unproxy the service proxy
     *
     * @param unwrapType The java type as which to unwrap this instance.
     * @return The unwrapped reference
     * @throws UnknownUnwrapTypeException if the servicebe unwrapped as the indicated type
     */
    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }
}

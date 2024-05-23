package org.ameba.tenancy;

import org.ameba.exception.TechnicalRuntimeException;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

/**
 * A TenantQueryDslConnectionProvider is a connection supplier based on {@code com.querydsl.sql.spring.SpringConnectionProvider} that also
 * handles the tenant.
 *
 * @author matjaz
 * @author Heiko Scherrer
 */
public class TenantQueryDslConnectionProvider implements Supplier<Connection> {

    // configured in org.springframework.context.annotation.MultiTenancySelector
    public static String defaultSchema;
    public static String tenantSchemaPrefix;

    private final DataSource dataSource;

    public TenantQueryDslConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection get() {
        var connection = DataSourceUtils.getConnection(dataSource);
        if (!DataSourceUtils.isConnectionTransactional(connection, dataSource)) {
            throw new IllegalStateException("Connection is not transactional");
        }

        // handle tenant
        try {
            return TenantSchemaUtils.applyTenantSchema(
                connection,
                TenantHolder.getCurrentTenant(),
                tenantSchemaPrefix,
                defaultSchema);
        } catch (SQLException e) {
            throw new TechnicalRuntimeException("Failed applying tenant schema to connection", e);
        }
    }
}

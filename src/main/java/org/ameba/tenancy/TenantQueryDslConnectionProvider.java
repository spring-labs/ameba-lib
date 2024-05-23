package org.ameba.tenancy;

import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

/**
 * Based on {@code com.querydsl.sql.spring.SpringConnectionProvider} but also handles the tenant.
 */
public class TenantQueryDslConnectionProvider implements Supplier<Connection> {

    // configured in org.springframework.context.annotation.MultiTenancySelector
    public static String defaultSchema;
    public static String tenantSchemaPrefix;

    private final DataSource dataSource;

    public TenantQueryDslConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection get() {
        Connection connection = DataSourceUtils.getConnection(dataSource);
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
            throw new RuntimeException("Failed applying tenant schema to connection", e);
        }
    }
}

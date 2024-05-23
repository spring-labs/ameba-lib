package org.ameba.tenancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class TenantSchemaUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantSchemaUtils.class);

    private TenantSchemaUtils() {}

    private static String getTenantSchema(String tenantId, String tenantSchemaPrefix, String defaultSchema) {
        return tenantId == null || tenantId.equals(defaultSchema) ?
            defaultSchema :
            tenantSchemaPrefix == null ? tenantId : tenantSchemaPrefix + tenantId;
    }

    public static Connection applyTenantSchema(Connection connection, String tenantIdentifier, String tenantSchemaPrefix,
            String defaultSchema) throws SQLException {
        String currentSchema = connection.getSchema();
        String requiredSchema = getTenantSchema(tenantIdentifier, tenantSchemaPrefix, defaultSchema);
        if (!Objects.equals(currentSchema, requiredSchema)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Switch from schema [{}] to schema [{}]", currentSchema, requiredSchema);
            }
            connection.setSchema(requiredSchema);
        }
        return connection;
    }
}

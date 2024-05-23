package org.ameba.tenancy;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
 * A TenantSchemaUtils is an utility class to collect functions according to multi-tenancy support.
 *
 * @author matjaz
 */
public class TenantSchemaUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantSchemaUtils.class);

    private TenantSchemaUtils() {}

    private static String getTenantSchema(String tenantId, String tenantSchemaPrefix, String defaultSchema) {
        return tenantId == null || tenantId.equals(defaultSchema) ?
            defaultSchema :
            tenantSchemaPrefix == null ? tenantId : tenantSchemaPrefix + tenantId;
    }

    /**
     * Change the schema of the given {@literal connection}.
     *
     * @param connection The connection instance to change the schema on
     * @param tenantIdentifier The tenant identifier attached to the current session
     * @param tenantSchemaPrefix Some optional prefix that is used with the tenantIdentifier
     * @param defaultSchema The default schema is used when no tenant information is present
     * @return The modified connection instance
     * @throws SQLException In case of modifying or accessing the connection fails
     */
    public static Connection applyTenantSchema(Connection connection, @Nullable String tenantIdentifier, @Nullable String tenantSchemaPrefix,
            String defaultSchema) throws SQLException {
        var currentSchema = connection.getSchema();
        var requiredSchema = getTenantSchema(tenantIdentifier, tenantSchemaPrefix, defaultSchema);
        if (!Objects.equals(currentSchema, requiredSchema)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Switch from schema [{}] to schema [{}]", currentSchema, requiredSchema);
            }
            connection.setSchema(requiredSchema);
        }
        return connection;
    }
}

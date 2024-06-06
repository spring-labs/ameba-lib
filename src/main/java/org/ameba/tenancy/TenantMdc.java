package org.ameba.tenancy;

import org.ameba.Constants;
import org.ameba.http.RequestIDHolder;
import org.slf4j.MDC;

/**
 * A TenantMdc simply wraps access to the MDC context object to be used at multiple places.
 *
 * @author Heiko Scherrer
 * @author matjaz
 */
public final class TenantMdc {

    private TenantMdc() {}

    /**
     * Set the {@literal tenant} into the current MDC context.
     *
     * @param tenant The tenant identifier
     */
    public static void setContext(String tenant) {
        MDC.put(Constants.HEADER_VALUE_X_TENANT, tenant);
        if (RequestIDHolder.hasRequestID()) {
            MDC.put(Constants.HEADER_VALUE_X_REQUESTID, RequestIDHolder.getRequestID());
        }
    }

    /**
     * Clear the whole MDC context.
     */
    public static void clearContext() {
        MDC.remove(Constants.HEADER_VALUE_X_TENANT);
        MDC.remove(Constants.HEADER_VALUE_X_REQUESTID);
    }
}

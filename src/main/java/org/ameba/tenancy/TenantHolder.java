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
package org.ameba.tenancy;

import java.util.function.Consumer;

/**
 * A TenantHolder stores a tenant inside a thread local variable.
 *
 * @author Heiko Scherrer
 * @version 1.2
 * @since 1.0
 */
public class TenantHolder {

    private static final InheritableThreadLocal<String> tenantHolder = new InheritableThreadLocal<>();

    /**
     * Private Constructor.
     */
    private TenantHolder() {}

    /**
     * Get the current Tenant set as String.
     *
     * @return Tenant as String
     */
    public static String getCurrentTenant() {
        return tenantHolder.get();
    }

    /**
     * Set current Tenant.
     *
     * @param tenant Tenant's id to set
     */
    public static void setCurrentTenant(String tenant) {
        tenantHolder.set(tenant);
    }

    /**
     * Set current Tenant.
     *
     * @param consumer A Tenant's id consumer function
     */
    public static void setCurrentTenant(Consumer<String> consumer) {
        if (tenantHolder.get() != null && !tenantHolder.get().isEmpty()) {
            consumer.accept(tenantHolder.get());
        }
    }

    /**
     * Cleanup thread local.
     */
    public static void destroy() {
        tenantHolder.remove();
    }
}

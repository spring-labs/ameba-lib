/*
 * Copyright 2014-2015 the original author or authors.
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
package org.ameba.http;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ameba.tenancy.TenantHolder;

/**
 * A MultiTenantSessionFilter set the current tenant value ({@value org.ameba.Constants#HEADER_VALUE_TENANT} in the
 * {@link TenantHolder TenantHolder}.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.0
 * @see TenantHolder
 * @since 1.0
 */
public class MultiTenantSessionFilter extends AbstractTenantAwareFilter {

    /**
     * {@inheritDoc}
     * If {@code tenant} is present, set it in the {@link TenantHolder TenantHolder}
     */
    @Override
    protected void doBefore(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String tenant) {
        if (null != tenant && !tenant.isEmpty()) {
            TenantHolder.setCurrentTenant(tenant);
        }
    }

    /**
     * {@inheritDoc}
     * Remove tenant from {@link TenantHolder TenantHolder}
     */
    @Override
    protected void doAfter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String tenant) {
        if (null != tenant && !tenant.isEmpty()) {
            TenantHolder.destroy();
        }
    }
}

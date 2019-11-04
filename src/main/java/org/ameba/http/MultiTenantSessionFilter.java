/*
 * Copyright 2014-2018 the original author or authors.
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

import org.ameba.LoggingCategories;
import org.ameba.tenancy.TenantHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A MultiTenantSessionFilter set the current tenant value ({@value org.ameba.Constants#HEADER_VALUE_X_TENANT} in the
 * {@link TenantHolder TenantHolder}.
 *
 * @author Heiko Scherrer
 * @version 1.0
 * @see TenantHolder
 * @since 1.0
 */
public class MultiTenantSessionFilter extends AbstractTenantAwareFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingCategories.BOOT);

    public MultiTenantSessionFilter() {
        LOGGER.info("Initialized filter {}", this.getClass().getSimpleName());
    }

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

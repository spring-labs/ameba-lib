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

import org.ameba.Constants;
import org.ameba.LoggingCategories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A SLF4JMappedDiagnosticContextFilter adds the current tenant to SLF4J's Mapped Diagnostics Context.
 *
 * @author Heiko Scherrer
 * @see org.slf4j.MDC
 * @since 0.9
 */
public class SLF4JMappedDiagnosticContextFilter extends AbstractTenantAwareFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingCategories.BOOT);

    public SLF4JMappedDiagnosticContextFilter() {
        LOGGER.info("Initialized filter {}", this.getClass().getSimpleName());
    }

    /**
     * {@inheritDoc} Set the MDC properly.
     */
    @Override
    protected void doBefore(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String tenant) {
        MDC.put(Constants.HEADER_VALUE_X_TENANT, tenant);
        if (RequestIDHolder.hasRequestID()) {
            MDC.put(Constants.HEADER_VALUE_X_REQUESTID, RequestIDHolder.getRequestID());
        }
    }

    /**
     * {@inheritDoc} Remove the tenant information from MDC.
     */
    @Override
    protected void doAfter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String tenant) {
        MDC.clear();
    }
}

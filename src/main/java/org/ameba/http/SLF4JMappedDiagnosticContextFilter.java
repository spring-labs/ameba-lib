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

import org.ameba.Constants;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A SLF4JMappedDiagnosticContextFilter adds the current tenant to SLF4J's Mapped Diagnostics Context.
 * The tenant information is expected to be present as http header attribute with name {@value Constants#HEADER_VALUE_TENANT}.
 * This
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.0
 * @see org.slf4j.MDC
 * @since 0.9
 */
public class SLF4JMappedDiagnosticContextFilter extends OncePerRequestFilter {

    /**
     * {@inheritDoc}
     * <p>
     * Set the MDC properly if multi tenancy support is enabled in the servletContext.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean multiTenancyEnabled = Boolean.valueOf((String) request.getServletContext().getAttribute(Constants.PARAM_MULTI_TENANCY_ENABLED));
        boolean throwIfNotPresent = Boolean.valueOf((String) request.getServletContext().getAttribute(Constants.PARAM_MULTI_TENANCY_THROW_IF_NOT_PRESENT));

        if (multiTenancyEnabled) {
            String tenant = request.getHeader(Constants.HEADER_VALUE_TENANT);
            if (throwIfNotPresent && (null == tenant || tenant.isEmpty())) {
                throw new IllegalArgumentException(String.format("No tenant information available in http header. Expected header %s attribute not present.", Constants.HEADER_VALUE_TENANT));
            }
            MDC.put(Constants.HEADER_VALUE_TENANT, tenant);
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(Constants.HEADER_VALUE_TENANT);
        }
    }
}

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
package org.ameba.http;

import org.ameba.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A AbstractTenantAwareFilter is a super class that handles resolution and validation of the current tenant from the incoming request.
 * <p>
 * The tenant information is expected to be present as http header attribute with name {@value Constants#HEADER_VALUE_X_TENANT}. The behavior
 * of the validation of attribute existence can be configured via two properties: <ul> <li>{@value
 * Constants#PARAM_MULTI_TENANCY_ENABLED}: If {@literal true}, the filter expects the tenant attribute set in the current request and goes
 * further with validation and processing the tenant information.</li> <li>{@value Constants#PARAM_MULTI_TENANCY_THROW_IF_NOT_PRESENT}: If
 * the tenant attribute is not present in the current request and this property is set to {@literal true}, the filter will throw an
 * exception. If set to {@literal false} the filter ignores the missing attribute and goes further in the {@code filterChain} without
 * calling the subclasses {@link #doBefore(HttpServletRequest, HttpServletResponse, FilterChain, String)} method</li> </ul>
 *
 * Those properties can be either set as {@code ServletContext} attributes or as {@link javax.servlet.FilterConfig} parameters whereas the
 * latter take precedence.
 *
 * @author Heiko Scherrer
 * @see org.springframework.web.filter.OncePerRequestFilter
 * @since 1.0
 */
public abstract class AbstractTenantAwareFilter extends OncePerRequestFilter {

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void doFilterInternal(HttpServletRequest r, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String fromSC = (String) r.getServletContext().getAttribute(Constants.PARAM_MULTI_TENANCY_ENABLED);
        boolean multiTenancyEnabled = Boolean.valueOf(fromSC == null ? getFilterConfig().getInitParameter(Constants.PARAM_MULTI_TENANCY_ENABLED) : fromSC);
        String tenant = null;
        if (multiTenancyEnabled && !"OPTIONS".equalsIgnoreCase(r.getMethod())) {
            tenant = r.getHeader(Constants.HEADER_VALUE_X_TENANT);
            if (null == tenant || tenant.isEmpty()) {
                String throwParam = (String) r.getServletContext().getAttribute(Constants.PARAM_MULTI_TENANCY_THROW_IF_NOT_PRESENT);
                boolean throwIfNotPresent = Boolean.valueOf(throwParam == null ? getFilterConfig().getInitParameter(Constants.PARAM_MULTI_TENANCY_THROW_IF_NOT_PRESENT) : throwParam);
                if (throwIfNotPresent) {
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    throw new IllegalArgumentException(String.format("No tenant information available in http header. Expected header [%s/%s] attribute not present.", Constants.HEADER_VALUE_X_TENANT, Constants.HEADER_VALUE_X_TENANT));
                }
            } else {
                doBefore(r, response, filterChain, tenant);
            }
        }
        try {
            filterChain.doFilter(r, response);
        } finally {
            doAfter(r, response, filterChain, tenant);
        }
    }

    /**
     * Do something before the {@code request} is passed to the next filter in the {@code filterChain}.
     *
     * @param request Incoming request
     * @param response Passed response
     * @param filterChain The chain of filters
     * @param tenant The current tenant
     */
    protected void doBefore(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String tenant) {
    }

    /**
     * Do something after the is through the {@code filterChain} and before it is passed to the next outer filter in chain.
     *
     * @param request Incoming request
     * @param response Passed response
     * @param filterChain The chain of filters
     * @param tenant The current tenant or {@literal null}
     */
    protected void doAfter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String tenant) {
    }
}

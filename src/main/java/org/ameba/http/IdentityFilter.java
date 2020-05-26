/*
 * Copyright 2005-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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

import static org.ameba.Constants.HEADER_VALUE_X_IDENTITY;

/**
 * An IdentityFilter expects and reads the identity information from a HTTP header and puts it into the current context variable.
 *
 * @author Heiko Scherrer
 */
public class IdentityFilter extends OncePerRequestFilter {

    /**
     * {@inheritDoc$}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String identity = request.getHeader(HEADER_VALUE_X_IDENTITY);
        if (null == identity || identity.isEmpty()) {
            String throwParam = (String) request.getServletContext().getAttribute(Constants.PARAM_IDENTITY_THROW_IF_NOT_PRESENT);
            boolean throwIfNotPresent = Boolean.valueOf(throwParam == null ? getFilterConfig().getInitParameter(Constants.PARAM_MULTI_TENANCY_THROW_IF_NOT_PRESENT) : throwParam);
            if (throwIfNotPresent) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                throw new IllegalArgumentException(String.format("No identity information available in http header. Expected header [%s] attribute not set", HEADER_VALUE_X_IDENTITY));
            }
        } else {
            IdentityContextHolder.setCurrentIdentity(identity);
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            IdentityContextHolder.destroy();
        }
    }
}

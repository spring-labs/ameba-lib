/*
 * Copyright 2005-2023 the original author or authors.
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
package org.ameba.http.identity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ameba.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.ameba.Constants.HEADER_VALUE_X_IDENTITY;

/**
 * An IdentityFilter expects and reads the identity information from an HTTP header and puts it into the current context
 * variable.
 *
 * @author Heiko Scherrer
 */
public class IdentityFilter extends OncePerRequestFilter {

    private final IdentityResolverStrategy strategy;

    public IdentityFilter(IdentityResolverStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String fromSC = (String) request.getServletContext().getAttribute(Constants.PARAM_IDENTITY_ENABLED);
        boolean identityEnabled = Boolean.valueOf(fromSC == null ? getFilterConfig().getInitParameter(Constants.PARAM_IDENTITY_ENABLED) : fromSC);
        if (identityEnabled) {

            Optional<Identity> identity = strategy.getIdentity(getHeaders(request));
            if (identity.isPresent()) {
                IdentityContextHolder.setCurrentIdentity(identity.get().getId());
            } else {
                String throwParam = (String) request.getServletContext().getAttribute(Constants.PARAM_IDENTITY_THROW_IF_NOT_PRESENT);
                boolean throwIfNotPresent = Boolean.valueOf(throwParam == null ? getFilterConfig().getInitParameter(Constants.PARAM_IDENTITY_THROW_IF_NOT_PRESENT) : throwParam);
                if (throwIfNotPresent) {
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    throw new IllegalArgumentException(String.format("No identity information available in http header. Expected header [%s] attribute not set", HEADER_VALUE_X_IDENTITY));
                }
            }
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            IdentityContextHolder.destroy();
        }
    }

    Map<String, List<String>> getHeaders(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        h -> Collections.list(request.getHeaders(h))
                ));
    }
}

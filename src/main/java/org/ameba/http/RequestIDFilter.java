/*
 * Copyright 2015-2023 the original author or authors.
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
import org.ameba.IDGenerator;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A RequestIDFilter.
 *
 * @author Heiko Scherrer
 * @since 1.7
 */
public class RequestIDFilter extends OncePerRequestFilter {

    private final IDGenerator<String> generator;

    public RequestIDFilter(IDGenerator<String> generator) {
        this.generator = generator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            if (request.getHeader(Constants.HEADER_VALUE_X_REQUESTID) == null) {
                RequestIDHolder.setRequestID(generator.generate());
            } else {
                RequestIDHolder.setRequestID(request.getHeader(Constants.HEADER_VALUE_X_REQUESTID));
            }
        try {
            filterChain.doFilter(request, response);
        } finally {
                RequestIDHolder.destroy();
        }
    }
}

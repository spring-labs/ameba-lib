/*
 * Copyright 2015-2024 the original author or authors.
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

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ameba.Constants;
import org.ameba.IDGenerator;
import org.ameba.LoggingCategories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A RequestIDFilter is responsible to attach a unique id to every incoming request. If the request has already an id assigned it is taken
 * over, otherwise a new id is generated (using an instance of {@link IDGenerator}) and attached to the current context.
 *
 * @author Heiko Scherrer
 */
public class RequestIDFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingCategories.BOOT);
    private final IDGenerator<String> generator;

    public RequestIDFilter(IDGenerator<String> generator) {
        LOGGER.info("Initialized filter {}", this.getClass().getSimpleName());
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

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
package org.ameba.oauth2;

import org.ameba.http.FilterStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * An OAuth2AuthenticationFilter is a Spring managed component that acts as a wrapper around
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
@Component
class OAuth2AuthenticationFilter extends OncePerRequestFilter {

    private final FilterStrategy strategy;

    public OAuth2AuthenticationFilter(@Qualifier("jwtValidationStrategy") FilterStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * {@inheritDoc}
     *
     * Delegate to JwtFilterStrategy.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        strategy.doFilter(request, response);
        filterChain.doFilter(request, response);
    }
}

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

import io.jsonwebtoken.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A OAuth2AuthenticationFilter.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
@Component
public class OAuth2AuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2AuthenticationFilter.class);
    private final List<TokenExtractor> extractors;
    private final TokenValidator validator;

    public OAuth2AuthenticationFilter(List<TokenExtractor> extractors, TokenValidator validator) {
        this.extractors = extractors;
        this.validator = validator;
    }

    /**
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && isBearer(authHeader)) {

            LOGGER.debug("Authorization Header detected, start extracting and validating...");
            Jwt jwt = extractToken(authHeader);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Extracted JWT: [{}]", jwt);
            }
            validator.validate(jwt, request);
        }
        filterChain.doFilter(request, response);
    }

    private Jwt extractToken(String authHeader) {
        List<ExtractionResult> result = extractors
                .stream()
                .map(extractor -> {
                    ExtractionResult vr = extractor.canExtract(authHeader);
                    return vr.isExtractionPossible() ? extractor.extract(authHeader) : vr;
                })
                .collect(Collectors.toList());

        ExtractionResult extractionResult = result
                .stream()
                .filter(ExtractionResult::hasJwt)
                .findFirst()
                .orElseThrow(() -> new InvalidTokenException("Could not extract JWT from token"));

        return extractionResult.getJwt();
    }

    private boolean isBearer(String authHeader) {
        return authHeader.startsWith("Bearer");
    }
}

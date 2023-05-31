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

import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;

import static java.util.Arrays.asList;
import static org.ameba.Constants.HEADER_VALUE_X_CALLERID;
import static org.ameba.Constants.HEADER_VALUE_X_CALL_CONTEXT;
import static org.ameba.Constants.HEADER_VALUE_X_IDENTITY;
import static org.ameba.Constants.HEADER_VALUE_X_REQUESTID;
import static org.ameba.Constants.HEADER_VALUE_X_TENANT;
import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;

/**
 * A PermitAllCorsConfigurationSource pre-configures and returns an {@link CorsConfiguration CorsConfiguration} instance to
 * allow all types of requests from any arbitrary origin domain.
 *
 * <b>This class may not be used in production systems and is foreseen only to be configured in development, test or CI
 * environments.</b>
 *
 * The combination of Spring's {@link org.springframework.web.filter.CorsFilter CorsFilter} and this configuration class, replaces
 * the need for the widely used {@code CorsDecorator}.
 *
 * To do so, simply define a Spring bean that returns the configured {@link org.springframework.web.filter.CorsFilter CorsFilter}.
 *
 * <pre>
 *
 *	public
 *	&#064;Profile(SpringProfiles.DEVELOPMENT_PROFILE)
 *  &#064;Bean
 *  Filter corsFiler() {
 *      return new CorsFilter(new PermitAllCorsConfigurationSource());
 *  }
 * </pre>
 *
 * @author Heiko Scherrer
 * @version 1.0
 * @since 1.5
 */
public class PermitAllCorsConfigurationSource implements CorsConfigurationSource {

    /**
     * {@inheritDoc}
     *
     * Returns a pre-configured configuration that allows everything from everywhere.
     */
    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.setAllowedMethods(asList(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(), HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name()));
        corsConfiguration.setAllowedHeaders(asList("X-REQUESTED-WITH",
                ACCEPT_LANGUAGE,
                CONTENT_TYPE,
                AUTHORIZATION,
                HEADER_VALUE_X_TENANT,
                HEADER_VALUE_X_REQUESTID,
                HEADER_VALUE_X_IDENTITY,
                HEADER_VALUE_X_CALLERID,
                HEADER_VALUE_X_CALL_CONTEXT
        ));
        corsConfiguration.setExposedHeaders(asList(
                LOCATION,
                HEADER_VALUE_X_TENANT,
                HEADER_VALUE_X_REQUESTID,
                HEADER_VALUE_X_IDENTITY,
                HEADER_VALUE_X_CALLERID,
                HEADER_VALUE_X_CALL_CONTEXT
        ));
        corsConfiguration.setMaxAge(1800L);
        return corsConfiguration;
    }
}

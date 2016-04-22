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

import static java.util.Arrays.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

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
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
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
        corsConfiguration.setAllowedMethods(asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(asList("Content-Type", "X-REQUESTED-WITH", "Authorization"));
        corsConfiguration.setMaxAge(1800L);
        return corsConfiguration;
    }
}

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
package org.ameba.http.identity;

import org.ameba.annotation.ExcludeFromScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;

/**
 * A IdentityConfiguration is not meant to be scanned by applications, therefor it is {@link ExcludeFromScan} and not in the {@literal app}
 * package. It is responsible to setup support for identity propagation.
 *
 * @author Heiko Scherrer
 */
@ExcludeFromScan
@Configuration
public class IdentityConfiguration {

    /** Set by selector! */
    public static boolean enabled = false;
    /** Set by selector! */
    public static boolean throwIfNotPresent = false;
    /** Set by selector! */
    public static String[] urlPatterns;
    /** Set by selector! */
    public static IdentityResolverStrategy strategy;

    /**
     * Registers the IdentityFilter filter.
     *
     * @return The registration bean instance
     */
    public
    @Bean
    FilterRegistrationBean identityFilterRegistrationBean() {
        FilterRegistrationBean<IdentityFilter> registration = new FilterRegistrationBean<>(new IdentityFilter(strategy));
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        if (urlPatterns != null) {
            registration.addUrlPatterns(urlPatterns);
        }
        registration.addInitParameter(org.ameba.Constants.PARAM_IDENTITY_ENABLED, String.valueOf(enabled));
        registration.addInitParameter(org.ameba.Constants.PARAM_IDENTITY_THROW_IF_NOT_PRESENT, String.valueOf(throwIfNotPresent));
        return registration;
    }

}

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

import javax.servlet.DispatcherType;

import org.ameba.annotation.ExcludeFromScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A MultiTenancyConfiguration is not meant to be scanned by applications, therefor it is {@link ExcludeFromScan} and not in the {@literal
 * app} package. It is responsible to setup support for multi-tenancy.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.0
 * @since 1.7
 */
@ExcludeFromScan
@Configuration
public class MultiTenancyConfiguration {

    /** Set by selector! */
    public static boolean enabled = false;
    /** Set by selector! */
    public static boolean throwIfNotPresent = false;

    /**
     * Registers the MultiTenantSessionFilter filter.
     *
     * @return The registration bean instance
     */
    public
    @Bean
    FilterRegistrationBean multiTenantSessionFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new MultiTenantSessionFilter());
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.addInitParameter(org.ameba.Constants.PARAM_MULTI_TENANCY_ENABLED, String.valueOf(enabled));
        registration.addInitParameter(org.ameba.Constants.PARAM_MULTI_TENANCY_THROW_IF_NOT_PRESENT, String.valueOf(throwIfNotPresent));
        return registration;
    }

    /**
     * Registers the SLF4JMappedDiagnosticContextFilter filter.
     *
     * @return The registration bean instance
     */
    public
    @Bean
    FilterRegistrationBean slf4JMappedDiagnosticContextFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new SLF4JMappedDiagnosticContextFilter());
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.addInitParameter(org.ameba.Constants.PARAM_MULTI_TENANCY_ENABLED, String.valueOf(enabled));
        registration.addInitParameter(org.ameba.Constants.PARAM_MULTI_TENANCY_THROW_IF_NOT_PRESENT, String.valueOf(throwIfNotPresent));
        return registration;
    }
}
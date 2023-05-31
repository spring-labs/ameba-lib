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
package org.ameba.app;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * A SecurityConfigurers class is a collector of interfaces that provide a configuration option for security related topics.
 *
 * @author Heiko Scherrer
 * @version 1.1
 * @since 1.4
 */
public final class SecurityConfigurers {

    /**
     * A SecurityConfigurers implementation is recognized by subclasses of Spring's {@link org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
     * WebSecurityConfigurerAdapter} and has the ability to configure the security through the given {@link
     * org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder AuthenticationManagerBuilder}.
     */
    public interface AuthenticationManagerConfigurer {

        /**
         * Let the implementing class configure the authentication mechanism.
         *
         * @param auth The builder object
         */
        default void configure(AuthenticationManagerBuilder auth) {
        }
    }

    public interface HttpSecurityConfigurer {

        /**
         * Let the implementing class configure the {@link HttpSecurity} instance.
         *
         * @param http The configuration object
         */
        default void configure(HttpSecurity http) {
        }
    }
}

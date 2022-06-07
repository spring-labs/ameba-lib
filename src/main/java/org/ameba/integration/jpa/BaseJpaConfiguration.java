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
package org.ameba.integration.jpa;

import org.ameba.annotation.ExcludeFromScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A BaseJpaConfiguration bootstraps all JPA features of the ameba module.
 *
 * @author Heiko Scherrer
 */
@ExcludeFromScan
@ConditionalOnClass(name = "org.springframework.data.domain.AuditorAware")
@Configuration
public class BaseJpaConfiguration {

    @ConditionalOnClass(name = {
            "org.springframework.data.domain.AuditorAware",
            "org.springframework.security.core.context.SecurityContextHolder"
    })
    @Bean
    public org.springframework.data.domain.AuditorAware<String> auditorProvider() {
      return new PrincipalProvider();
    }

    @ConditionalOnClass(name = {
            "org.springframework.data.domain.AuditorAware"
    })
    @ConditionalOnMissingClass("org.springframework.security.core.context.SecurityContextHolder")
    @Bean
    public org.springframework.data.domain.AuditorAware<String> identityAuditorProvider() {
        return new IdentityPrincipalProvider();
    }
}

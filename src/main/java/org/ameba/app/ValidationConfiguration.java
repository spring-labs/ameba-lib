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

import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * A ValidationConfiguration bootstraps the JSR-303 Validator.
 *
 * @author Heiko Scherrer
 */
@ConditionalOnClass(LocalValidatorFactoryBean.class)
@Configuration
public class ValidationConfiguration {

    /**
     * Provides a bean instance to get a JSR-303 validator from.
     * 
     * @param messageSource (Optional) messageSource to inject
     * @return The factory bean
     */
    @ConditionalOnMissingBean
    public @Bean Validator messageSourceAwareValidator(@Autowired(required = false) MessageSource messageSource) {
        var bean = new LocalValidatorFactoryBean();
        if (messageSource != null) {
            bean.setValidationMessageSource(messageSource);
        }
        return bean;
    }
}

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
package org.ameba.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

/**
 * A BaseConfiguration enables instantiates a JSR-303 ValidatorFactory.
 *
 * @author Heiko Scherrer
 * @version 1.1
 * @since 1.0
 */
@Configuration
public class BaseConfiguration {

    /**
     * Provides a bean instance to get a JSR-303 validator from.
     * 
     * @param messageSource (Optional) messageSource to inject
     * @return The factory bean
     */
    @Primary
    @Bean
    public Validator messageSourceAwareValidator(@Autowired(required = false) MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        if (messageSource != null) {
            bean.setValidationMessageSource(messageSource);
        }
        return bean;
    }
}

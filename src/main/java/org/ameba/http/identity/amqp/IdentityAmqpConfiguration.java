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
package org.ameba.http.identity.amqp;

import org.ameba.amqp.MessageHeaderEnhancer;
import org.ameba.amqp.MessagePostProcessorProvider;
import org.ameba.annotation.ExcludeFromScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A IdentityConfiguration is not meant to be scanned by applications, therefor it is {@link ExcludeFromScan} and not in the {@literal app}
 * package. It is responsible to setup support for identity propagation.
 *
 * @author Heiko Scherrer
 */
@ExcludeFromScan
@Configuration
public class IdentityAmqpConfiguration {

    @ConditionalOnClass(org.springframework.amqp.rabbit.core.RabbitTemplate.class)
    public @Bean MessagePostProcessorProvider IdentityCFDecorator() {
        return new IdentityHeaderResolver();
    }

    @ConditionalOnClass(org.springframework.amqp.rabbit.core.RabbitTemplate.class)
    public @Bean MessageHeaderEnhancer identityEnhancer() {
        return new IdentityEnhancer();
    }
}

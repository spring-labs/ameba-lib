/*
 * Copyright 2005-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ameba.amqp;

import org.ameba.annotation.ExcludeFromScan;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * A AmqpConfiguration.
 *
 * @author Heiko Scherrer
 */
@ExcludeFromScan
@ConditionalOnClass(org.springframework.amqp.rabbit.core.RabbitTemplate.class)
@AutoConfiguration
public class AmqpConfiguration {

    @Bean(name = "rabbitListenerContainerFactory")
    @ConditionalOnMissingBean(name = "rabbitListenerContainerFactory")
    @ConditionalOnProperty(prefix = "spring.rabbitmq.listener", name = "type", havingValue = "simple", matchIfMissing = true)
    SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurerDecorator configurer, ConnectionFactory connectionFactory) {
        var factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    public @Bean SimpleRabbitListenerContainerFactoryConfigurerDecorator simpleRabbitListenerCFC(RabbitProperties rabbitProperties,
            SimpleRabbitListenerContainerFactoryConfigurer simpleRabbitListenerContainerFactoryConfigurer,
            @Autowired(required = false) List<MessagePostProcessorProvider> messagePostProcessorProviders) {
        return new SimpleRabbitListenerContainerFactoryConfigurerDecorator(rabbitProperties,
                simpleRabbitListenerContainerFactoryConfigurer,
                messagePostProcessorProviders);
    }

    public @Bean RabbitTemplateConfigurable rabbitTemplateConfigurable(List<MessageHeaderEnhancer> headerEnhancers) {
        return new RabbitTemplateConfigurerAdapter(headerEnhancers);
    }
}

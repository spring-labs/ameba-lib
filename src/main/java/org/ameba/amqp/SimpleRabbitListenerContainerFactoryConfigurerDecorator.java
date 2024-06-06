/*
 * Copyright 2005-2023 the original author or authors.
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

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.AbstractRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;

import java.util.List;

/**
 * A SimpleRabbitListenerContainerFactoryConfigurerDecorator takes the {@link SimpleRabbitListenerContainerFactoryConfigurer} from
 * SpringBoot and decorates it with additional functionality. This decorator only works with an override of the {@code SimpleRabbitListenerContainerFactory}.
 *
 * @author Heiko Scherrer
 */
public class SimpleRabbitListenerContainerFactoryConfigurerDecorator
        extends AbstractRabbitListenerContainerFactoryConfigurer<SimpleRabbitListenerContainerFactory> {

    private final SimpleRabbitListenerContainerFactoryConfigurer simpleRabbitListenerContainerFactoryConfigurer;

    private List<MessagePostProcessorProvider> messagePostProcessorProviders;

    protected SimpleRabbitListenerContainerFactoryConfigurerDecorator(RabbitProperties rabbitProperties,
            SimpleRabbitListenerContainerFactoryConfigurer simpleRabbitListenerContainerFactoryConfigurer,
            List<MessagePostProcessorProvider> messagePostProcessorProviders) {
        super(rabbitProperties);
        this.simpleRabbitListenerContainerFactoryConfigurer = simpleRabbitListenerContainerFactoryConfigurer;
        this.messagePostProcessorProviders = messagePostProcessorProviders;
    }

    @Override
    public void configure(SimpleRabbitListenerContainerFactory factory, ConnectionFactory connectionFactory) {
        if (messagePostProcessorProviders != null) {
            factory.setAfterReceivePostProcessors(messagePostProcessorProviders.stream()
                    .map(MessagePostProcessorProvider::getMessagePostProcessor)
                    .toArray(MessagePostProcessor[]::new)
            );
        }
        simpleRabbitListenerContainerFactoryConfigurer.configure(factory, connectionFactory);
    }
}

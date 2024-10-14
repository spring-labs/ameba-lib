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

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;

/**
 * A RabbitTemplateConfigurerAdapter is using {@link MessageHeaderEnhancer} instances to configure the {@link RabbitTemplate}.
 *
 * @author Heiko Scherrer
 */
public class RabbitTemplateConfigurerAdapter implements RabbitTemplateConfigurable {

    private final List<MessageHeaderEnhancer> headerEnhancers;

    public RabbitTemplateConfigurerAdapter(List<MessageHeaderEnhancer> headerEnhancers) {
        this.headerEnhancers = headerEnhancers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure(RabbitTemplate rabbitTemplate) {
        if (headerEnhancers != null) {
            headerEnhancers.forEach(he -> he.enhance(rabbitTemplate));
        }
    }
}

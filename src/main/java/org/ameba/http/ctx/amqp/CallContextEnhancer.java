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
package org.ameba.http.ctx.amqp;

import org.ameba.amqp.MessageHeaderEnhancer;
import org.ameba.http.ctx.CallContextHolder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * A CallContextEnhancer enhances AMQP messages about a header ({@code owms_callcontext}) to propagate the {@code CallContext}.
 *
 * @author Heiko Scherrer
 */
public class CallContextEnhancer implements MessageHeaderEnhancer {

    /**
     * {@inheritDoc}
     */
    @Override
    public void enhance(final RabbitTemplate rabbitTemplate) {
        rabbitTemplate.addBeforePublishPostProcessors(
            m -> {
                if (CallContextHolder.getEncodedCallContext().isPresent()) {
                    m.getMessageProperties().getHeaders().put("owms_callcontext", CallContextHolder.getEncodedCallContext().get());
                }
                return m;
            }
        );
    }
}

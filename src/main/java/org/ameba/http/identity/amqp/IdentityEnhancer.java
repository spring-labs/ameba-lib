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
package org.ameba.http.identity.amqp;

import org.ameba.amqp.MessageHeaderEnhancer;
import org.ameba.http.identity.IdentityContextHolder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * A IdentityEnhancer enhances AMQP messages about a header ({@code owms_identity}) to propagate the {@code Identity}.
 *
 * @author Heiko Scherrer
 */
class IdentityEnhancer implements MessageHeaderEnhancer {

    @Override
    public void enhance(final RabbitTemplate rabbitTemplate) {
        rabbitTemplate.addBeforePublishPostProcessors(
            m -> {
                if (IdentityContextHolder.currentIdentity().isPresent()) {
                    m.getMessageProperties().getHeaders().put("owms_identity", IdentityContextHolder.getCurrentIdentity());
                }
                return m;
            }
        );
    }
}

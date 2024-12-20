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
package org.ameba.http.identity.amqp;

import org.ameba.amqp.MessagePostProcessorProvider;
import org.ameba.http.identity.IdentityContextHolder;
import org.springframework.amqp.core.MessagePostProcessor;

/**
 * A IdentityHeaderResolver resolves the {@code Identity} from the AMQP message header ({@code owms_identity}) and stores it in the current
 * thread context.
 *
 * @author Heiko Scherrer
 */
class IdentityHeaderResolver implements MessagePostProcessorProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public MessagePostProcessor getMessagePostProcessor() {
        return (m -> {
            if (m.getMessageProperties().getHeaders().containsKey("owms_identity")) {
                IdentityContextHolder.setCurrentIdentity((String) m.getMessageProperties().getHeaders().get("owms_identity"));
            }
            return m;
        });
    }
}

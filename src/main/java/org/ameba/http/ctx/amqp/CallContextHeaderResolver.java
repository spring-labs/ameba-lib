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
package org.ameba.http.ctx.amqp;

import org.ameba.amqp.MessagePostProcessorProvider;
import org.ameba.http.ctx.CallContextHolder;
import org.ameba.http.ctx.CallContextProvider;
import org.springframework.amqp.core.MessagePostProcessor;

/**
 * A CallContextHeaderResolver.
 *
 * @author Heiko Scherrer
 */
class CallContextHeaderResolver implements MessagePostProcessorProvider {

    private final CallContextProvider callContextProvider;

    CallContextHeaderResolver(CallContextProvider callContextProvider) {
        this.callContextProvider = callContextProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessagePostProcessor getMessagePostProcessor() {
        return (m -> {
            if (m.getMessageProperties().getHeaders().containsKey("owms_callcontext")) {
                CallContextHolder.setCallContext(
                        () -> (String) m.getMessageProperties().getHeaders().get("owms_callcontext"),
                        callContextProvider.getInitialCallContext());
            }
            return m;
        });
    }
}

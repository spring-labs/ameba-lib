/*
 * Copyright 2005-2022 the original author or authors.
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

/**
 * A MessagePostProcessorProvider provides a {@link MessagePostProcessor} to enhance the container factory.
 *
 * @author Heiko Scherrer
 */
public interface MessagePostProcessorProvider {

    /**
     * Return a {@link MessagePostProcessor} to add to the ContainerFactory in order to post-prosess incoming AMQP messages.
     *
     * @return The instance
     */
    MessagePostProcessor getMessagePostProcessor();
}
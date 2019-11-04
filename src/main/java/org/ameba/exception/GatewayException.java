/*
 * Copyright 2014-2018 the original author or authors.
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
package org.ameba.exception;

import org.ameba.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

/**
 * A GatewayException is a general exception to signal that a depending backend service did not respond as expected.
 *
 * @author Heiko Scherrer
 */
@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class GatewayException extends BusinessRuntimeException {

    /**
     * Constructs with a message key and a message
     *
     * @param messageKey Message key
     * @param data    Additional implicit data passed to the caller
     */
    public GatewayException(String messageKey, Serializable[] data) {
        super(messageKey, data);
    }

    /**
     * Constructs with a message text a message key and a message
     *
     * @param message Message text
     * @param messageKey Message key
     * @param data    Additional implicit data passed to the caller
     */
    public GatewayException(String message, String messageKey, Serializable[] data) {
        super(message, messageKey, data);
    }

    /**
     * Create and return an instance with the given parameters and the message key {@link Messages#GW_GENERIC}.
     *
     * @param message Message text
     * @param data Additional implicit data passed to the caller
     * @return The instance
     */
    public static GatewayException createFrom(String message, Serializable... data) {
        return new GatewayException(message, Messages.GW_GENERIC, data);
    }
}

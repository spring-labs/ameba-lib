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
package org.ameba.exception;

import org.ameba.i18n.Translator;

import java.io.Serializable;

/**
 * A BusinessRuntimeException expresses a business error with some useful business information.
 *
 * @author Heiko Scherrer
 * @since 0.3
 */
public class BusinessRuntimeException extends RuntimeException {

    private String messageKey;
    private Serializable[] data;

    /** Default constructor. */
    public BusinessRuntimeException() {
    }

    /**
     * Constructor with message text.
     *
     * @param message Message text
     */
    public BusinessRuntimeException(String message) {
        super(message);
    }

    /**
     * Constructor with message text and cause exception.
     *
     * @param message Message text
     * @param cause Cause exception
     */
    public BusinessRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with message text.
     *
     * @param message Message text
     * @param messageKey Message key
     */
    public BusinessRuntimeException(String message, String messageKey) {
        super(message);
        this.messageKey = messageKey;
    }

    /**
     * Constructs with a message key and a message
     *
     * @param message Message text
     * @param messageKey Message key
     * @param data Additional implicit data passed to the caller
     */
    public BusinessRuntimeException(String message, String messageKey, Serializable[] data) {
        super(message);
        this.messageKey = messageKey;
        this.data = data;
    }

    /**
     * Constructs with a message key and a message
     *
     * @param messageKey Message key
     * @param data Additional implicit data passed to the caller
     */
    public BusinessRuntimeException(String messageKey, Serializable[] data) {
        super();
        this.messageKey = messageKey;
        this.data = data;
    }

    /**
     * Use a {@link Translator} instance to resolve the {@code message} from the {@code messageKey}.
     *
     * @param translator A Translator instance to use
     * @param messageKey The key to resolve the i18n aware message text from
     * @param data Arbitrary data used in the message text
     * @param param Additional implicit data passed to the caller
     */
    public BusinessRuntimeException(Translator translator, String messageKey, Serializable[] data, Object... param) {
        this(translator.translate(messageKey, param), messageKey, data);
    }

    /**
     * Get the message key.
     *
     * @return The message key
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * Get the data, applied to the exception.
     *
     * @return The applied data, the caller should know about how to use this information
     */
    public Serializable[] getData() {
        return data;
    }
}

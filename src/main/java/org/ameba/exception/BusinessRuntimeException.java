/*
 * Copyright 2014-2015 the original author or authors.
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

import java.io.Serializable;

/**
 * A BusinessRuntimeException expresses a business error with some useful business information.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @since 0.3
 */
public class BusinessRuntimeException extends RuntimeException {

    private String msgKey;
    private Serializable[] data;

    /** Constructor. */
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
     * @param msgKey Message key
     */
    public BusinessRuntimeException(String message, String msgKey) {
        super(message);
        this.msgKey = msgKey;
    }

    /**
     * Constructs with a message key and a message
     *
     * @param message Message text
     * @param msgKey Message key
     * @param data Additional implicit data passed to the caller
     */
    public BusinessRuntimeException(String message, String msgKey, Serializable[] data) {
        super(message);
        this.msgKey = msgKey;
        this.data = data;
    }

    /**
     * Constructs with a message key and a message
     *
     * @param msgKey Message key
     * @param data Additional implicit data passed to the caller
     */
    public BusinessRuntimeException(String msgKey, Serializable[] data) {
        super();
        this.msgKey = msgKey;
        this.data = data;
    }

    /**
     * Get the message key.
     *
     * @return The message key
     */
    public String getMsgKey() {
        return msgKey;
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

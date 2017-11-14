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
 * A TechnicalRuntimeException keeps the message text of the superclass together with a message key (used for translation) and a translated
 * message text
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.0
 * @since 1.3
 */
public class TechnicalRuntimeException extends RuntimeException {

    private String msgKey;
    private Serializable[] data;

    /** Default constructor. */
    public TechnicalRuntimeException() {
        super();
    }

    /**
     * Constructor with mesaage.
     *
     * @param message The message
     */
    public TechnicalRuntimeException(String message) {
        super(message);
    }

    /**
     * Create a TechnicalRuntimeException with message and root cause.
     *
     * @param message The message text
     * @param cause The root cause
     */
    public TechnicalRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create a TechnicalRuntimeException with message and message key.
     *
     * @param message The message text
     * @param messageKey The message key
     */
    public TechnicalRuntimeException(String message, String messageKey) {
        super(message);
        this.msgKey = messageKey;
    }

    /**
     * Constructs with a message key and a message
     *
     * @param message Message text
     * @param messageKey Message key
     * @param data Additional implicit data passed to the caller
     */
    public TechnicalRuntimeException(String message, String messageKey, Serializable[] data) {
        super(message);
        this.msgKey = messageKey;
        this.data = data;
    }

    /**
     * Constructs with a message key and a message
     *
     * @param messageKey Message key
     * @param data Additional implicit data passed to the caller
     */
    public TechnicalRuntimeException(String messageKey, Serializable[] data) {
        super();
        this.msgKey = messageKey;
        this.data = data;
    }

    /**
     * Create a TechnicalRuntimeException with message, message key and root cause.
     *
     * @param message The message text
     * @param messageKey The message key
     * @param cause The root cause
     */
    public TechnicalRuntimeException(String message, String messageKey, Throwable cause) {
        super(message, cause);
        this.msgKey = messageKey;
    }

    private TechnicalRuntimeException(Builder builder) {
        msgKey = builder.msgKey;
        data = builder.data;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Get the message key.
     *
     * @return The message key
     */
    public String getMessageKey() {
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


    public static final class Builder {
        private String msgKey;
        private Serializable[] data;

        private Builder() {
        }

        public Builder withMsgKey(String val) {
            msgKey = val;
            return this;
        }

        public Builder withData(Serializable[] val) {
            data = val;
            return this;
        }

        public TechnicalRuntimeException build() {
            return new TechnicalRuntimeException(this);
        }
    }
}

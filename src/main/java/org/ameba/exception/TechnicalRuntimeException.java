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

    /**
     * Default constructor.
     */
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
     * Create a TechnicalRuntimeException with message and root cause.
     *
     * @param message The message text
     * @param cause The root cause
     */
    public TechnicalRuntimeException(String message, Throwable cause) {
        super(message, cause);
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

    /**
     * Get the message key.
     *
     * @return The message key
     */
    public String getMessageKey() {
        return msgKey;
    }
}

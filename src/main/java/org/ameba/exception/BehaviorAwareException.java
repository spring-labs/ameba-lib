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


import org.ameba.http.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * A BehaviorAwareException is used to group exceptions that express a kind of behavior, like 'an entity to look up was not found'.
 *
 * @author Heiko Scherrer
 * @since 0.2
 */
public abstract class BehaviorAwareException extends BusinessRuntimeException {

    /** Default constructor. */
    public BehaviorAwareException() {
        super();
    }

    /**
     * Construct with a message.
     *
     * @param message The message
     */
    public BehaviorAwareException(String message) {
        super(message);
    }

    /**
     * Construct with a message and a message key.
     *
     * @param message The message
     * @param messageKey The message key
     */
    public BehaviorAwareException(String message, String messageKey) {
        super(message, messageKey);
    }

    /**
     * Construct with a message, a message key and parameters stored with the exception.
     *
     * @param message The message
     * @param messageKey The message key
     * @param data Data passed to clients
     */
    protected BehaviorAwareException(String message, String messageKey, Serializable... data) {
        super(message, messageKey, data);
    }

    /**
     * Transform exception into an {@code ResponseEntity} and return it.
     *
     * @return The ResponseEntity
     */
    public ResponseEntity<Response> toResponse() {
        return new ResponseEntity<>(Response.newBuilder().withMessage(getMessage()).withMessageKey(getMessageKey()).withHttpStatus(getStatus().toString()).withObj(getData()).build(), getStatus());
    }

    /**
     * Transform exception into an {@code ResponseEntity} and return it.
     *
     * @param data Additional implicit data passed to the caller
     * @return The ResponseEntity
     */
    public ResponseEntity<Response> toResponse(Serializable... data) {
        return new ResponseEntity<>(Response.newBuilder().withMessage(getMessage()).withMessageKey(getMessageKey()).withHttpStatus(getStatus().toString()).withObj(getData()).build(), getStatus());
    }

    /**
     * What status to deliver to the client?
     *
     * @return The HttpStatus
     */
    public abstract HttpStatus getStatus();
}

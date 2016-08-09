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

import org.ameba.http.AbstractBase;
import org.ameba.http.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * A BehaviorAwareException is used to group exceptions that express a kind of behavior, like 'an entity to look up was not found'.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 0.3
 * @since 0.2
 */
public abstract class BehaviorAwareException extends BusinessRuntimeException {

    /**
     * {@inheritDoc}
     */
    public BehaviorAwareException() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public BehaviorAwareException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public BehaviorAwareException(String message, String msgKey) {
        super(message, msgKey);
    }

    /**
     * {@inheritDoc}
     */
    protected BehaviorAwareException(String message, String msgKey, Serializable... data) {
        super(message, msgKey, data);
    }

    /**
     * Transform exception into an {@code ResponseEntity} and return it.
     *
     * @return The ResponseEntity
     */
    public ResponseEntity<Response<AbstractBase>> toResponse() {
        return new ResponseEntity<>(new Response<>(this.getMessage(), this.getMsgKey(), getStatus().toString(), new AbstractBase[0]), getStatus());
    }

    /**
     * What status to deliver to the client?
     *
     * @return The HttpStatus
     */
    public abstract HttpStatus getStatus();
}

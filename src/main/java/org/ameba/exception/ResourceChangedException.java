/*
 * Copyright 2015-2024 the original author or authors.
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
 * A ResourceChangedException signals that a resource cannot be changed, because it has been updated in the meantime. Similar what a
 * java.persistence.OptimisticLockException expresses on the persistence layer, this type of exception is more generic and useful at the
 * presentation layer.
 *
 * @author Heiko Scherrer
 * @version 0.3
 * @see org.springframework.web.bind.annotation.ResponseStatus
 * @see org.springframework.http.HttpStatus#CONFLICT
 * @since 0.2
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceChangedException extends BehaviorAwareException {

    /**
     * Preset the message to {@link Messages#RESOURCE_CHANGED_UNEXPECTEDLY}.
     */
    public ResourceChangedException() {
        super(Messages.RESOURCE_CHANGED_UNEXPECTEDLY, Messages.RESOURCE_CHANGED_UNEXPECTEDLY);
    }

    /**
     * {@inheritDoc}
     */
    public ResourceChangedException(String message) {
        super(message, Messages.RESOURCE_CHANGED_UNEXPECTEDLY);
    }

    /**
     * {@inheritDoc}
     */
    public ResourceChangedException(String message, String msgKey) {
        super(message, msgKey);
    }

    /**
     * {@inheritDoc}
     */
    protected ResourceChangedException(String message, String msgKey, Serializable... data) {
        super(message, msgKey, data);
    }

    /**
     * {@link HttpStatus#CONFLICT}.
     *
     * @return {@link HttpStatus#CONFLICT}
     */
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}

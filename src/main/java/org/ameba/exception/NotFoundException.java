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

import org.ameba.Messages;
import org.ameba.i18n.Translator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A NotFoundException is used to signal an entity, expected to exist but was not found. This class is annotated with Springs {@link
 * ResponseStatus} and results in a HTTP response with status {@link org.springframework.http.HttpStatus#NOT_FOUND}
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 0.4
 * @see org.springframework.web.bind.annotation.ResponseStatus
 * @see org.springframework.http.HttpStatus#NOT_FOUND
 * @since 0.2
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends BehaviorAwareException {

    /**
     * Preset the message to {@link Messages#NOT_FOUND}.
     */
    public NotFoundException() {
        super(Messages.NOT_FOUND, Messages.NOT_FOUND);
    }

    /**
     * {@inheritDoc}
     */
    public NotFoundException(String message) {
        super(message,  Messages.NOT_FOUND);
    }

    /**
     * {@inheritDoc}
     */
    public NotFoundException(String message, String msgKey) {
        super(message, msgKey);
    }

    /**
     * {@inheritDoc}
     */
    public NotFoundException(String message, String msgKey, Serializable... data) {
        super(message, msgKey, data);
    }

    /**
     * {@inheritDoc}
     */
    public NotFoundException(Translator translator) {
        super(translator.translate(Messages.NOT_FOUND), Messages.NOT_FOUND);
    }

    /**
     * {@inheritDoc}
     */
    public NotFoundException(Translator translator, String msgKey, Object... param) {
        super(translator.translate(msgKey, param), msgKey);
    }

    /*~ factory methods */
    /**
     * Throw a NotFoundException when {@code obj} is {@literal null}.
     *
     * @param obj The instance to check
     * @param translator A Translator instance
     * @param msgKey The message key
     * @param param Message parameters
     */
    public static void throwIfNull(Object obj, Translator translator, String msgKey, Object... param) {
        if (obj == null) {
            throw new NotFoundException(translator, msgKey, param);
        }
    }

    /**
     * Throw a NotFoundException when {@code obj} is {@literal null}.
     *
     * @param obj The instance to check
     * @param message The message text for the exception
     */
    public static void throwIfNull(Object obj, String message) {
        if (obj == null) {
            throw new NotFoundException(message);
        }
    }

    /**
     * {@link HttpStatus#NOT_FOUND}.
     *
     * @return {@link HttpStatus#NOT_FOUND}
     */
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}

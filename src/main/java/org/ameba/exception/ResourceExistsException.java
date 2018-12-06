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
 * A ResourceExistsException is used to signal a conflicting behavior in case a resource exists whereas its existence
 * was not expected.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 0.3
 * @since 0.2
 * @see org.springframework.web.bind.annotation.ResponseStatus
 * @see org.springframework.http.HttpStatus#CONFLICT
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceExistsException extends BehaviorAwareException {

	/**
	 * {@inheritDoc}
	 */
	public ResourceExistsException() {
        super(Messages.ALREADY_EXISTS, Messages.ALREADY_EXISTS);
	}

	/**
	 * {@inheritDoc}
	 */
	public ResourceExistsException(String message) {
		super(message, Messages.ALREADY_EXISTS);
	}

	/**
     * {@inheritDoc}
     */
    public ResourceExistsException(String message, String msgKey) {
		super(message, msgKey);
	}

	/**
	 * {@inheritDoc}
	 */
	public ResourceExistsException(String message, String msgKey, Serializable... data) {
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

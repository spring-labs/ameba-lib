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
 * A BusinessRuntimeException expresses a business error with a business message key ({@code msgKey}).
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 0.1
 * @since 0.3
 */
public class BusinessRuntimeException extends RuntimeException {

	private String msgKey;

	/**
	 * Constructor.
	 */
	public BusinessRuntimeException() {}

	/**
	 * Constructor with message text.
	 *
	 * @param message
	 *            Message text
	 */
	public BusinessRuntimeException(String message) {
		super(message);
	}

	/**
	 * Constructor with message text and cause exception.
	 *
	 * @param message
	 *            Message text
	 * @param cause
	 *            Cause exception
	 */
	public BusinessRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor with message text.
	 *
	 * @param message
	 *            Message text
	 * @param msgKey
	 *            Message key
	 */
	public BusinessRuntimeException(String message, String msgKey) {
		super(message);
		this.msgKey = msgKey;
	}

	/**
	 * Get the message key.
	 *
	 * @return The message key
	 */
	public String getMsgKey() {
		return msgKey;
	}
}

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
package ch.abraxas.exception;

/**
 * A PresentationLayerException.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 0.1
 * @since 0.2
 */
public class PresentationLayerException extends RuntimeException {

	/**
	 * {@inheritDoc}
	 */
	public PresentationLayerException() {
	}

	/**
	 * {@inheritDoc}
	 */
	public PresentationLayerException(String message) {
		super(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public PresentationLayerException(String message, Throwable cause) {
		super(message, cause);
	}
}

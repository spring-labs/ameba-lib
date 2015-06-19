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

import org.ameba.Messages;
import org.ameba.i18n.Translator;
import org.springframework.http.HttpStatus;

/**
 * A ResourceChangedException signals that a resource cannot be changed, because it has been updated in the meantime.
 * Similar what a java.persistence.OptimisticLockException expresses on the persistence layer, this type of exception
 * is more generic and useful at the presentation layer.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 0.2
 * @since 0.2
 */
public class ResourceChangedException extends AbstractBehaviorAwareException {

	/**
	 * Preset the message to {@link Messages#RESOURCE_CHANGED_UNEXPECTEDLY}.
	 */
	public ResourceChangedException() {
		super(Messages.RESOURCE_CHANGED_UNEXPECTEDLY);
	}

	private ResourceChangedException(String message, String msgKey) {
		super(message, msgKey);
	}

	/**
	 * Create a generic ResourceChangedException with the translated message set.
	 *
	 * @param translator A Translator
	 * @return The instance
	 */
	public static ResourceChangedException createChanged(Translator translator) {
		return new ResourceChangedException(translator.translate(Messages.RESOURCE_CHANGED_UNEXPECTEDLY), Messages.RESOURCE_CHANGED_UNEXPECTEDLY);
	}

	/**
	 * {@link HttpStatus#CONFLICT}.
	 *
	 * @return {@link HttpStatus#CONFLICT}
	 */
	@Override
	protected HttpStatus getStatus() {
		return HttpStatus.CONFLICT;
	}
}

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

import ch.abraxas.Messages;
import ch.abraxas.i18n.Translator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A ResourceExistsException is used to signal a conflicting behavior in case a resource exists whereas its existence
 * was not expected.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 0.2
 * @since 0.2
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceExistsException extends AbstractBehaviorAwareException {

	/**
	 * Preset the message to {@link Messages#ALREADY_EXISTS}.
	 */
	public ResourceExistsException() {
        super(Messages.ALREADY_EXISTS);
	}

	private ResourceExistsException(String message, String msgKey) {
		super(message, msgKey);
	}

	/**
	 * Create a generic ResourceExistsException with the translated message set.
	 *
	 * @param translator A Translator
	 * @return The instance
	 */
	public static ResourceExistsException createExists(Translator translator) {
		return new ResourceExistsException(translator.translate(Messages.ALREADY_EXISTS), Messages.ALREADY_EXISTS);
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

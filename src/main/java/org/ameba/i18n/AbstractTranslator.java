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
package org.ameba.i18n;

import org.springframework.context.MessageSource;

/**
 * A AbstractTranslator expects to translate messages from a Spring managed {@link MessageSource}. Subclasses may
 * provide their own {@link MessageSource}s that way.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 0.1
 * @since 0.3
 */
public abstract class AbstractTranslator implements Translator {

    /**
     * {@inheritDoc}
     */
    @Override
    public String translate(String key, Object... objects) {
        return getMessageSource().getMessage(key, objects, null);
    }

	/**
	 * Provide a {@code MessageSource} that is used to translate the message.
	 *
	 * @return The {@code MessageSource}
	 */
    protected abstract MessageSource getMessageSource();
}

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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A NotFoundException is used that an entity that should be looked up was not found. This class is marked to be
 * transformed into a HTTP response with status {@link org.springframework.http.HttpStatus#NOT_FOUND}
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.0.0
 * @since 1.0.0
 * @see org.springframework.web.bind.annotation.ResponseStatus
 * @see org.springframework.http.HttpStatus#NOT_FOUND
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends AbstractBehaviorAwareException {

    public NotFoundException(){
        super(Messages.NOT_FOUND);
    }

    @Override
    protected HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}

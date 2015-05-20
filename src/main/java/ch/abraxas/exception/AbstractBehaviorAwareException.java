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


import ch.abraxas.http.AbstractBase;
import ch.abraxas.http.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * A AbstractBehaviorAwareException is used to group exceptions that express a kind of behavior, like 'an entity to look
 * up was not found'.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractBehaviorAwareException extends RuntimeException {

    /**
     * {@inheritDoc}
     */
    public AbstractBehaviorAwareException() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public AbstractBehaviorAwareException(String message) {
        super(message);
    }

    /**
     * Transform exception into an {@code ResponseEntity} and return it.
     *
     * @return The ResponseEntity
     */
    public ResponseEntity<Response> toResponse() {
        AbstractBase[] ab = new AbstractBase[0];
        return new ResponseEntity<>(new Response(this.getMessage(), this.getMessage(), getStatus().toString(), ab), getStatus());
    }

    /**
     * What status to deliver to the client?
     *
     * @return The HttpStatus
     */
    protected abstract HttpStatus getStatus();
}

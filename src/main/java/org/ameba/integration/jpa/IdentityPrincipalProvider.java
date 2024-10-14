/*
 * Copyright 2005-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ameba.integration.jpa;

import org.ameba.http.identity.IdentityContextHolder;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * A IdentityPrincipalProvider is an adapter class used to propagate the current principal to the Spring Data context in order to be used
 * for auditing modifications on entity classes. It is used if Spring Security is not on the classpath and resolves the current principal
 * only from the Ameba Identity Context.
 *
 * @author Heiko Scherrer
 */
class IdentityPrincipalProvider implements AuditorAware<String> {

    /**
     * {@inheritDoc}
     *
     * Only try to resolve the principal from the {@code IdentityContext}.
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        if (IdentityContextHolder.getCurrentIdentity() != null) {
            return Optional.of(IdentityContextHolder.getCurrentIdentity());
        }
        return Optional.empty();
    }
}

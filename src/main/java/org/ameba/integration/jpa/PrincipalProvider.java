/*
 * Copyright 2005-2022 the original author or authors.
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
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * A PrincipalProvider is an adapter class used to propagate the current principal to the Spring Data context in order to be used for
 * auditing modifications on entity classes.
 *
 * @author Heiko Scherrer
 */
class PrincipalProvider implements AuditorAware<String> {

    /**
     * {@inheritDoc}
     *
     * First try to resolve the principal from the {@code IdentityContext}, afterwards from Spring's {@code SecurityContext}.
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        if (IdentityContextHolder.getCurrentIdentity() != null) {
            return Optional.of(IdentityContextHolder.getCurrentIdentity());
        } else if (SecurityContextHolder.getContext() != null &&
                SecurityContextHolder.getContext().getAuthentication() != null) {
            return Optional.of(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        return Optional.empty();
    }
}

/*
 * Copyright 2015-2024 the original author or authors.
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
package org.ameba.integration.jpa;

import jakarta.validation.constraints.NotNull;
import org.ameba.exception.NotFoundException;
import org.ameba.integration.FindOperations;
import org.ameba.integration.TypedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.Collection;

/**
 * A GenericJpaRepositories.
 *
 * @author Heiko Scherrer
 * @version 1.0
 * @since 1.5
 * @param <ID> any serializable type
 * @param <T> any TypedEntity type
 */
public interface JpaRepositoryOperations<T extends TypedEntity<ID>, ID extends Serializable> {

    /**
     * Implementations must provide the concrete repository they're working on.
     *
     * @return A typed JpaRepository
     */
    JpaRepository<T, ID> getRepository();

    /**
     * Adapter to {@link FindOperations FindOperations}.
     *
     * @param <ID> any serializable type
     * @param <T> any TypedEntity type
     */
    interface Find<T extends TypedEntity<ID>, ID extends Serializable> extends JpaRepositoryOperations<T, ID>, FindOperations<T, ID> {

        /**
         * {@inheritDoc}
         *
         * Returns an empty {@link java.util.ArrayList} in case of null or empty
         */
        @Override
        default @NotNull Collection<T> findAll() {
            return getRepository().findAll();
        }

        /**
         * {@inheritDoc}
         *
         * Find and return the instance, throw a NotFoundException if no entity was found
         */
        @Override
        default @NotNull T findById(@NotNull ID id) {
            return getRepository().findById(id).orElseThrow(() -> new NotFoundException(String.format("No entity with id %s found", id)));
        }
    }
}

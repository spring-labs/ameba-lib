/*
 * Copyright 2015-2020 the original author or authors.
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
package org.ameba.integration;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

/**
 * A FindOperations.
 *
 * @author Heiko Scherrer
 * @version 1.0
 * @since 1.0
 */
public interface FindOperations<T extends TypedEntity<ID>, ID extends Serializable> {

    /**
     * Find and return a collection of all existing entities of type {@code T}.
     *
     * @return All entities or an empty collection, never {@literal null}
     */
    @NotNull Collection<T> findAll();

    /**
     * Find an entity instance by the given technical key {@code id},
     *
     * @param id The technical key
     * @return The instance
     * @throws org.ameba.exception.NotFoundException may be thrown if no entity found
     */
    @NotNull T findById(@NotNull ID id);
}

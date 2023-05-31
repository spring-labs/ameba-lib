/*
 * Copyright 2015-2023 the original author or authors.
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

/**
 * A SaveOperations.
 *
 * @author Heiko Scherrer
 * @since 1.6
 */
public interface SaveOperations<T extends TypedEntity<ID>, ID extends Serializable> {

    /**
     * Saves the state of the passed {@code entity} no matter whether the instance already exists or does not exist.
     *
     * @param entity The instance to save
     * @return The saved instance
     */
    @NotNull T save(@NotNull T entity);
}

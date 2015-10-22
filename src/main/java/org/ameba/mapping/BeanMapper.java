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
package org.ameba.mapping;

import java.util.List;

/**
 * A BeanMapper is used to map Java classes (e.g. JavaBeans) between different domain models.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.0
 * @since 0.7
 */
public interface BeanMapper {

    /**
     * Convert an entity instance of type {@code S} to an instance of type {@code T}.
     *
     * @param entity The entity instance to convert from
     * @param clazz The target entity class type
     * @param <S> The source entity to convert
     * @param <T> The target type to convert to
     * @return A new instance of the target type
     */
    <S, T> T map(S entity, Class<T> clazz);

    /**
     * Convert an entity instance to a target instance considering the entity class types.
     *
     * @param source The entity instance to convert from
     * @param target The target instance to convert to
     * @param <S> The source entity to convert
     * @param <T> The target entity to convert
     * @return The source instance
     */
    <S, T> T mapFromTo(S source, T target);

    /**
     * Convert a {@link java.util.List List} of entities to a particular target type.
     *
     * @param entities The List of entity instances to convert from
     * @param clazz The target entity class type.
     * @param <S> The source entities to convert
     * @param <T> The target type to convert to
     * @return The converted entities
     */
    <S, T> List<T> map(List<S> entities, Class<T> clazz);
}

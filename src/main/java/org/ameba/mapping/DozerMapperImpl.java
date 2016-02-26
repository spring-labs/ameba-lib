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

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * A DozerMapperImpl uses the Open Source Dozer project to automatically map between bean classes.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.0
 * @since 0.7
 */
public class DozerMapperImpl implements BeanMapper {

    private Mapper mapper;

    /**
     * Constructor of this Bean must have a {@code Mapper Mapper} instance of the SF Dozer library.
     *
     * @param mapper A SF Dozer mapper instance
     */
    public DozerMapperImpl(Mapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Constructor of this Bean takes a couple of Dozer mapping files that are used to instantiate the underlying Dozer mapper.
     *
     * @param mappingFiles Dozer mapping files
     */
    public DozerMapperImpl(String... mappingFiles) {
        this.mapper = new DozerBeanMapper(asList(mappingFiles));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Garbage in - garbage out principle. In case a caller uses a repository implementation that may
     * return {@code null} in times of Java 8, a {@code null} value will simply be returned. It is not the job of this
     * library to decide on {@code null} values.
     */
    @Override
    public <S, T> T map(S entity, Class<T> clazz) {
        if (entity == null) {
            return null;
        }
        return mapper.map(entity, clazz);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Garbage in - garbage out principle. In case a caller uses a repository implementation that may
     * return {@code null} in times of Java 8, a {@code null} value will simply be returned. It is not the job of this
     * library to decide on {@code null} values.
     */
    @Override
    public <S, T> T mapFromTo(S source, T target) {
        T result = target;
        if (source == null) {
            result = null;
        } else {
            mapper.map(source, result);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The caller gets at least an empty {@code java.util.List List} implementation when {@code entities}
     * is {@code null} or empty.
     */
    @Override
    public <S, T> List<T> map(List<S> entities, Class<T> clazz) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>(0);
        }
        List<T> result = new ArrayList<>(entities.size());
        for (S entity : entities) {
            result.add(mapper.map(entity, clazz));
        }
        return result;
    }
}

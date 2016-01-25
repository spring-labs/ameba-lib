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

import org.dozer.DozerConverter;

import java.time.LocalDate;

/**
 * A LocalDateConverter is a Dozer converter to recognize java.time.LocalDate types. No conversion is applied,
 * but a type converter must be configured within Dozer, to accept Java 1.8 types. As soon as issue #172 https://github.com/DozerMapper/dozer/issues/172
 * is closed, this implementation can be deleted.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.0
 * @since 1.3.0
 */
public class LocalDateConverter extends DozerConverter<LocalDate, LocalDate>  {

    /**
     * {@inheritDoc}
     */
    public LocalDateConverter() {
        super(LocalDate.class, LocalDate.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate convertTo(LocalDate source, LocalDate destination) {
        return source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate convertFrom(LocalDate source, LocalDate destination) {
        return convertTo(source, destination);
    }
}
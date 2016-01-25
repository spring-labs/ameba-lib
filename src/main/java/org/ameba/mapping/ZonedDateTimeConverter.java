/* 
 * Copyright 2015 by Abraxas Informatik AG,
 * Waltersbachstrasse 5, CH-8006 Zuerich
 * All rights reserved.
 */
package org.ameba.mapping;

import java.time.ZonedDateTime;

import org.dozer.DozerConverter;

/**
 * A ZonedDateTimeConverter is a Dozer converter to recognize java.time.ZonedDateTime types. No conversion is applied,
 * but a type converter must be configured within Dozer, to accept Java 1.8 types.
 * As soon as these types are included into Dozer this implementation can be deleted.
 *
 * @author <a href="mailto:matteo.kamm@abraxas.ch">Matteo Kamm</a>
 * @version 1.0
 * @since 1.3.0
 */
public class ZonedDateTimeConverter extends DozerConverter<ZonedDateTime, ZonedDateTime> {

    /**
     * {@inheritDoc}
     */
    public ZonedDateTimeConverter() {
        super(ZonedDateTime.class, ZonedDateTime.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ZonedDateTime convertTo(ZonedDateTime source, ZonedDateTime destination) {
        return source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ZonedDateTime convertFrom(ZonedDateTime source, ZonedDateTime destination) {
        return convertTo(source, destination);
    }
}

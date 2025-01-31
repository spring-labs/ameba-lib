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
package org.ameba.http;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Abstract base class adds Spring HATEOAS support.
 *
 * @author Heiko Scherrer
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
public abstract class AbstractBase<T extends AbstractBase<? extends T>> extends RepresentationModel<T> {

    public static final String DATETIME_FORMAT_ZULU = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'";

    /** The current version of the Value Object (VO) derived from the version of the Entity Object (EO). */
    @JsonProperty("ol")
    private Long ol;

    /** Timestamp when the entity has been inserted. */
    @JsonProperty("createDt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT_ZULU) // required
    private LocalDateTime createDt;

    /** Timestamp when the entity has been updated the last time. */
    @JsonProperty("lastModifiedDt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT_ZULU) // required
    private LocalDateTime lastModifiedDt;

    /**
     * Get the current version.
     *
     * @return As Long
     */
    public Long getOl() {
        return ol;
    }

    /**
     * Set the current version.
     *
     * @param ol As Long
     */
    public void setOl(Long ol) {
        this.ol = ol;
    }

    /**
     * Get the creation date.
     *
     * @return The createDt
     */
    public LocalDateTime getCreateDt() {
        return createDt;
    }

    /**
     * Set the creation date.
     *
     * @param createDt The createDt
     */
    public void setCreateDt(LocalDateTime createDt) {
        this.createDt = createDt;
    }

    /**
     * Get the last modification date.
     *
     * @return The lastModifiedDt
     */
    public LocalDateTime getLastModifiedDt() {
        return lastModifiedDt;
    }

    /**
     * Set the last modification date.
     *
     * @param lastModifiedDt The lastModifiedDt
     */
    public void setLastModifiedDt(LocalDateTime lastModifiedDt) {
        this.lastModifiedDt = lastModifiedDt;
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AbstractBase<?> that = (AbstractBase<?>) o;
        return Objects.equals(ol, that.ol) && Objects.equals(createDt, that.createDt) && Objects.equals(lastModifiedDt, that.lastModifiedDt);
    }

    /**
     * {@inheritDoc}
     *
     * All fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ol, createDt, lastModifiedDt);
    }
}

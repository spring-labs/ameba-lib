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
package org.ameba.http.ctx;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * A CallContext stores technical information about the current request execution.
 *
 * @author Heiko Scherrer
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class CallContext implements Serializable {

    /** The name or ID of the calling process. */
    private String caller;
    /** The traceId of the current call context. */
    private String traceId;
    /** Arbitrary details populated as part of the {@link CallContext}. */
    private Map<String, Serializable> details = new HashMap<>();

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getTraceId() {
        return traceId;
    }

    @JsonIgnore
    public Optional<String> getOptionalTraceId() {
        return Optional.ofNullable(traceId);
    }

    void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Map<String, Serializable> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Serializable> details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CallContext)) return false;
        CallContext that = (CallContext) o;
        return Objects.equals(caller, that.caller) && Objects.equals(traceId, that.traceId) && Objects.equals(details, that.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caller, traceId, details);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CallContext.class.getSimpleName() + "[", "]")
                .add("caller='" + caller + "'")
                .add("traceId='" + traceId + "'")
                .add("details=" + details)
                .toString();
    }
}

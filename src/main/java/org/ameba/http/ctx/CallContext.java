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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A CallContext stores technical information about the current request execution.
 *
 * @author Heiko Scherrer
 */
public final class CallContext {

    /** The name or ID of the calling process. */
    private String caller;
    /** Arbitrary details populated as part of the {@link CallContext}. */
    private Map<String, Serializable> details = new HashMap<>();

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public Map<String, Serializable> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Serializable> details) {
        this.details = details;
    }
}

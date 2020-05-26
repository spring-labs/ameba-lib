/*
 * Copyright 2005-2020 the original author or authors.
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
package org.ameba.http.identity;

import java.util.Map;

/**
 * A SimpleIdentity.
 *
 * @author Heiko Scherrer
 */
public final class SimpleIdentity implements Identity {

    private final String id;
    private Map<String, String> details;

    public SimpleIdentity(String id) {
        this.id = id;
    }

    public SimpleIdentity(String id, Map<String, String> details) {
        this.id = id;
        this.details = details;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getDetails() {
        return null;
    }

    /**
     * Set the details of the Identity.
     *
     * @param details As a Map
     */
    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
}

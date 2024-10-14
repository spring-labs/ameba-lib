/*
 * Copyright 2005-2024 the original author or authors.
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
 * A SimpleIdentity is the simplest possible implementation of {@link Identity}.
 *
 * @author Heiko Scherrer
 */
public final class SimpleIdentity implements Identity {

    private final String id;
    private Map<String, String> details;

    /**
     * This method initializes a SimpleIdentity object with the provided ID. The ID must not be null or empty.
     *
     * @param id The ID of the SimpleIdentity
     * @throws IllegalArgumentException If the ID is null or empty
     */
    public SimpleIdentity(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID of SimpleIdentity must not be null");
        }
        this.id = id;
    }

    /**
     * Initializes a SimpleIdentity object with the provided ID and details.
     *
     * @param id The ID of the SimpleIdentity. Must not be null or empty.
     * @param details A map of attributes that belong to the Identity.
     * @throws IllegalArgumentException If the ID is null or empty.
     */
    public SimpleIdentity(String id, Map<String, String> details) {
        this(id);
        this.details = details;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getDetails() {
        return details;
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

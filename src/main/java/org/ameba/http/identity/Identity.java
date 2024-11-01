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
 * An Identity represents the human user's identity.
 *
 * @author Heiko Scherrer
 */
public interface Identity {

    /**
     * Get the unique identifier of the Identity instance.
     *
     * @return As String
     */
    String getId();

    /**
     * Get a map of attributes that belong to the Identity.
     *
     * @return A key-value structure of details
     */
    Map<String, String> getDetails();
}

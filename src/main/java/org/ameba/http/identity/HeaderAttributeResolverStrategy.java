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

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.ameba.Constants.HEADER_VALUE_X_IDENTITY;

/**
 * A HeaderAttributeResolverStrategy.
 *
 * @author Heiko Scherrer
 */
public class HeaderAttributeResolverStrategy implements IdentityResolverStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Identity> getIdentity(Map<String, List<String>> headers, Map<String, String> bodyParts,
                                          Map<String, String> queryParams) {
        var identity = headers.entrySet().stream()
                .filter(e -> e.getKey().equalsIgnoreCase(HEADER_VALUE_X_IDENTITY))
                .findFirst()
                .map(Map.Entry::getValue);
        if (identity.isEmpty() || identity.get().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new SimpleIdentity(identity.get().getFirst()));
    }
}

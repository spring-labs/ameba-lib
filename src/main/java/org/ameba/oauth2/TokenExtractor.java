/*
 * Copyright 2015-2023 the original author or authors.
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
package org.ameba.oauth2;

/**
 * A TokenExtractor is able to extract a structure token format from a String.
 *
 * @author Heiko Scherrer
 */
public interface TokenExtractor {

    /**
     * Is the implementation able to extract a token from the given {@code token} String?
     *
     * @param token The String
     * @return {@literal true} if so, otherwise {@literal false}
     */
    ExtractionResult canExtract(String token);

    /**
     * Extract valuable information from the given {@code token} String.
     *
     * @param token The String
     * @return The result
     * @throws InvalidTokenException in case extraction is not possible
     */
    ExtractionResult extract(String token);
}

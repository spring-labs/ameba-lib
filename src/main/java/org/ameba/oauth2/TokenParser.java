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
package org.ameba.oauth2;

import io.jsonwebtoken.Jwt;

/**
 * A TokenParser is able to parse tokens as Strings into JWT types.
 *
 * @author Heiko Scherrer
 */
public interface TokenParser<T extends Issuer, U extends Jwt> {

    /**
     * Returns the signing algorithm that is supported by the parser.
     *
     * @return As shortcut, see <a href="https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-31#section-4.1.1"></a>
     */
    String supportAlgorithm();

    /**
     * Parse the given {@code token} as String into a valid kind of JWT type.
     *
     * @param token The String token to parse
     * @param issuer The origin token issuer that provides the signing key
     * @return The parsed JWT instance
     */
    U parse(String token, T issuer);
}

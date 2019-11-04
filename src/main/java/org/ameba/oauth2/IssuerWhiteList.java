/*
 * Copyright 2014-2018 the original author or authors.
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
 * A IssuerWhiteList manages and stores all allowed Issuers.
 *
 * @author Heiko Scherrer
 */
public interface IssuerWhiteList<T extends Issuer> {

    /**
     * Resolve the Issuer by the given {@code issuerId}.
     *
     * @param issuerId The unique ID of the Issuer, see {@link Issuer#getIssuerId()}
     * @return The Issuer instance
     * @throws InvalidTokenException in case the Issuer can not be resolved
     */
    T getIssuer(String issuerId);
}

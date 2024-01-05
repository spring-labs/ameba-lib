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
package org.ameba.oauth2.issuer;

import org.ameba.oauth2.Issuer;

import java.net.URL;
import java.util.Optional;

/**
 * A IssuerRepository.
 *
 * @author Heiko Scherrer
 */
public interface IssuerRepository {

    /**
     * The URL of the issuing party.
     *
     * @param issUrl As a URL object
     * @return The issuer instance
     */
    Optional<Issuer> findByIssUrl(URL issUrl);

    /**
     * The URL of the issuing party.
     *
     * @param issUrl As a URL object
     * @param kid The Key ID of the JWT header
     * @return The issuer instance
     */
    Optional<Issuer> findByIssUrlAndKid(URL issUrl, String kid);
}

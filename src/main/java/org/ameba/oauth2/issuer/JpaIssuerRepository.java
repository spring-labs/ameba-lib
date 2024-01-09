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

import org.springframework.data.jpa.repository.JpaRepository;

import java.net.URL;
import java.util.List;
import java.util.Optional;

/**
 * A JpaIssuerRepository.
 *
 * @author Heiko Scherrer
 */
public interface JpaIssuerRepository extends JpaRepository<IssuerEO, Long> {

    List<IssuerEO> findByIssUrl(URL issUrl);

    Optional<IssuerEO> findByIssUrlAndKid(URL issUrl, String kid);
}

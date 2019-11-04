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

import java.net.URL;

/**
 * An Asymmetric Issuer can provide the Public Key to validate token signatures.
 *
 * @author Heiko Scherrer
 */
public interface Asymmetric extends Issuer {

    /**
     * Return the KID in terms of JWK.
     *
     * @return As String
     */
    String getKID();

    /**
     * Return the URL where to resolve JWK information from.
     *
     * @return The JWK endpoint URL
     */
    URL getJWKURL();
}

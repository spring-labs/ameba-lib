/*
 * Copyright 2014-2015 the original author or authors.
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
 * An Issuer is authorized to issue OAuth2 tokens.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
public interface Issuer {

    /**
     * The unique ID of the issuer, e.g. an URL (in case of ReadHat KeyCloak).
     *
     * @return As String
     */
    String getIssuerId();

    /**
     * Leap seconds allowed when a token signature is validated.
     *
     * @return In seconds
     */
    long getSkewSeconds();

    /**
     * Return the base URL of the authorization server.
     *
     * @return As URL
     */
    URL getBaseURL();
}
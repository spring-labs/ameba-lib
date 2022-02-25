/*
 * Copyright 2005-2022 the original author or authors.
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
package org.ameba.http.identity.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.ameba.http.identity.IdentityContextHolder;

import static org.ameba.Constants.HEADER_VALUE_X_IDENTITY;

/**
 * A IdentityRequestInterceptor is a Feign interceptor to propagate the Identity.
 *
 * @author Heiko Scherrer
 */
class IdentityRequestInterceptor implements RequestInterceptor {

    /**
     * {@inheritDoc}
     *
     * Add the Identity to outgoing requests.
     */
    @Override
    public void apply(RequestTemplate template) {
        IdentityContextHolder.currentIdentity().ifPresent(s -> template.header(HEADER_VALUE_X_IDENTITY, s));
    }
}

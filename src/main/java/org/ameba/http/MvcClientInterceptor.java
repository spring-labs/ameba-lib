/*
 * Copyright 2005-2023 the original author or authors.
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
package org.ameba.http;

import org.ameba.Constants;
import org.ameba.http.identity.IdentityContextHolder;
import org.ameba.tenancy.TenantHolder;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * A MvcClientInterceptor intercepts outgoing client requests in MVC contexts.
 *
 * @author Heiko Scherrer
 */
@Deprecated
public class MvcClientInterceptor implements ClientHttpRequestInterceptor {

    /**
     * {@inheritDoc}
     *
     * Add the {@value Constants#HEADER_VALUE_X_TENANT} header.
     * Add the {@value Constants#HEADER_VALUE_X_IDENTITY} header.
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = execution.execute(request, body);
        TenantHolder.setCurrentTenant((d) -> response.getHeaders().add(Constants.HEADER_VALUE_X_TENANT, d));
        IdentityContextHolder.setCurrentIdentity((d) -> response.getHeaders().add(Constants.HEADER_VALUE_X_IDENTITY, d));
        return response;
    }
}

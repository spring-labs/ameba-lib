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
package org.ameba.http.multitenancy;

import org.ameba.Constants;
import org.ameba.http.BaseClientHttpRequestInterceptor;
import org.ameba.tenancy.TenantHolder;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * A TenantClientRequestInterceptor propagates the Tenant with the RestTemplate.
 *
 * @author Heiko Scherrer
 */
public class TenantClientRequestInterceptor implements BaseClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        TenantHolder.currentTenant().ifPresent(s -> request.getHeaders().add(Constants.HEADER_VALUE_X_TENANT, s));
        return execution.execute(request, body);
    }
}

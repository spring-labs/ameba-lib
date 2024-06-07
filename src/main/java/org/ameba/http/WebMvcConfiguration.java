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

import org.ameba.annotation.ExcludeFromScan;
import org.ameba.http.ctx.CallContextClientRequestInterceptor;
import org.ameba.http.identity.IdentityClientRequestInterceptor;
import org.ameba.http.multitenancy.TenantClientRequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * A WebMvcConfiguration configures Servlet environments with the interceptors for CallContext and Identity propagation.
 *
 * @author Heiko Scherrer
 */
@ExcludeFromScan
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfiguration
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * {@inheritDoc}
     *
     * Propagate context information with the RestTemplate.
     */
    public @Bean List<BaseClientHttpRequestInterceptor> baseRestTemplateInterceptors() {
        return new ArrayList<>(asList(
                new CallContextClientRequestInterceptor(),
                new IdentityClientRequestInterceptor(),
                new TenantClientRequestInterceptor()
        ));
    }
}

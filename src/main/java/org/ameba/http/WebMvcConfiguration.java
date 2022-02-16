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
package org.ameba.http;

import org.ameba.app.BaseClientHttpRequestInterceptor;
import org.ameba.http.ctx.CallContextClientRequestInterceptor;
import org.ameba.http.ctx.CallContextConfiguration;
import org.ameba.http.identity.IdentityClientRequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Arrays.asList;

/**
 * A WebMvcConfiguration.
 *
 * @author Heiko Scherrer
 */
@ConditionalOnClass(org.springframework.web.servlet.HandlerInterceptor.class)
@Configuration
@Import(CallContextConfiguration.class)
public class WebMvcConfiguration {

    public @Bean
    Supplier<List<BaseClientHttpRequestInterceptor>> baseRestTemplateInterceptors() {
        return () -> new ArrayList<>(asList(
                new CallContextClientRequestInterceptor(),
                new IdentityClientRequestInterceptor()
        ));
    }

    @LoadBalanced
    @Bean
    RestTemplate aLoadBalanced(List<BaseClientHttpRequestInterceptor> baseInterceptors) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().addAll(baseInterceptors);
        return restTemplate;
    }


}

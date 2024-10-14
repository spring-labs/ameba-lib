/*
 * Copyright 2015-2024 the original author or authors.
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
package org.ameba.http.ctx;

import org.ameba.annotation.ExcludeFromScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * A CallContextWebMvcConfiguration enables CallContext handling and propagation.
 *
 * @author Heiko Scherrer
 */
@ExcludeFromScan
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfiguration
public class CallContextWebMvcConfiguration implements WebMvcConfigurer {

    private CallContextProvider callContextProvider;

    @Autowired
    public void setCallContextProvider(CallContextProvider callContextProvider) {
        this.callContextProvider = callContextProvider;
    }

    /**
     * {@inheritDoc}
     *
     * Activate interceptor to receive the CallContext
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(callContextInterceptor());
    }

    public @Bean CallContextInterceptor callContextInterceptor() {
        return new CallContextInterceptor(callContextProvider);
    }
}

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
package org.ameba.http.ctx;

import org.ameba.annotation.ExcludeFromScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;

/**
 * A DefaultCallContextProviderConfiguration provides the default (fallback) CallContextProvider if no Sleuth is used.
 *
 * @author Heiko Scherrer
 */
@ExcludeFromScan
@ConditionalOnMissingClass("org.springframework.cloud.sleuth.Tracer")
@AutoConfiguration
public class DefaultCallContextProviderConfiguration {

    @ConditionalOnMissingBean(name = "traceableCallContextProvider")
    @Bean
    public CallContextProvider callContextProvider() {
        return new SimpleCallContextProvider();
    }
}

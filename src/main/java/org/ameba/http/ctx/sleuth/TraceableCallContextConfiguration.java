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
package org.ameba.http.ctx.sleuth;

import org.ameba.LoggingCategories;
import org.ameba.annotation.ExcludeFromScan;
import org.ameba.http.ctx.CallContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Bean;

/**
 * A TraceableCallContextConfiguration provides a CallContextProvider suitable for traceable environments.
 *
 * @author Heiko Scherrer
 */
@ExcludeFromScan
@ConditionalOnClass(name = "org.springframework.cloud.sleuth.Tracer")
@AutoConfiguration
public class TraceableCallContextConfiguration {

    private static final Logger BOOT_LOGGER = LoggerFactory.getLogger(LoggingCategories.BOOT);

    @ConditionalOnClass(name = "org.springframework.cloud.sleuth.Tracer")
    @Bean(name = "traceableCallContextProvider")
    public CallContextProvider traceableCallContextProvider(Tracer tracer,
            @Value("${spring.application.name:#{null}}") String applicationName) {
        BOOT_LOGGER.info("-- w/ Sleuth Tracing");
        return new TraceableCallContextProvider(tracer, applicationName);
    }
}

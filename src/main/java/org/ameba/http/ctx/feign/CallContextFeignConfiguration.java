/*
 * Copyright 2015-2020 the original author or authors.
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
package org.ameba.http.ctx.feign;

import org.ameba.annotation.ExcludeFromScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A CallContextConfiguration enables CallContext propagation with the Feign client.
 *
 * @author Heiko Scherrer
 * @since 3.0
 */
@ExcludeFromScan
@ConditionalOnClass(feign.RequestInterceptor.class)
@Configuration
public class CallContextFeignConfiguration {

    public @Bean CallContextRequestInterceptor callContextRequestInterceptor() {
        return new CallContextRequestInterceptor();
    }
}

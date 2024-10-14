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
package org.ameba.http.ctx;

import org.ameba.Constants;
import org.ameba.http.BaseClientHttpRequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static org.ameba.LoggingCategories.CALL_CONTEXT;

/**
 * A CallContextClientRequestInterceptor propagates the CallContext with the RestTemplate.
 *
 * @author Heiko Scherrer
 */
public class CallContextClientRequestInterceptor implements BaseClientHttpRequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CALL_CONTEXT);

    /**
     * {@inheritDoc}
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        var ctx = CallContextHolder.getEncodedCallContext();
        if (ctx.isPresent()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("CTXClientRequestInterceptor: Add encoded CallContext to outgoing request [{}]", ctx.get());
            }
            request.getHeaders().add(Constants.HEADER_VALUE_X_CALL_CONTEXT, ctx.get());
        }
        return execution.execute(request, body);
    }
}

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
package org.ameba.http.ctx;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.ameba.Constants.HEADER_VALUE_X_CALL_CONTEXT;
import static org.ameba.LoggingCategories.CALL_CONTEXT;

/**
 * A CallContextRequestInterceptor is a Feign interceptor to propagate the CallContext.
 *
 * @author Heiko Scherrer
 */
class CallContextRequestInterceptor implements RequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CALL_CONTEXT);

    /**
     * {@inheritDoc}
     *
     * Add the CallContext to outgoing requests.
     */
    @Override
    public void apply(RequestTemplate template) {
        var ctx = CallContextHolder.getCallContextEncoded();
        if (ctx.isPresent()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Feign: CallContext to propagate is available [{}]", CallContextHolder.getCallContext());
            }
            template.header(HEADER_VALUE_X_CALL_CONTEXT, ctx.get());
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Feign: No CallContext to propagate available");
            }
        }
    }
}

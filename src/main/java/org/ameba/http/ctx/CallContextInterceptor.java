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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.ameba.Constants.HEADER_VALUE_X_CALLERID;
import static org.ameba.Constants.HEADER_VALUE_X_CALL_CONTEXT;
import static org.ameba.LoggingCategories.CALL_CONTEXT;

/**
 * A CallContextInterceptor resolves the CallContext from a request and initializes the current CallContext.
 *
 * @author Heiko Scherrer
 */
class CallContextInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CALL_CONTEXT);
    private final CallContextProvider callContextProvider;

    public CallContextInterceptor(CallContextProvider callContextProvider) {
        this.callContextProvider = callContextProvider;
    }

    /**
     * {@inheritDoc}
     *
     * <ul>
     *     <li>Resolve the CallContext header</li>
     *     <li>Resolve the CallerId header</li>
     * </ul>
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("CTXInterceptor: Enter preHandle");
        }
        CallContextHolder.setCallContext(() -> request.getHeader(HEADER_VALUE_X_CALL_CONTEXT), callContextProvider.getInitialCallContext());
        CallContextHolder.setCaller(() -> request.getHeader(HEADER_VALUE_X_CALLERID));
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * {@inheritDoc}
     *
     * Destroy the initialized CallContext.
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("CTXInterceptor: Enter postHandle");
        }
        CallContextHolder.destroy();
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}

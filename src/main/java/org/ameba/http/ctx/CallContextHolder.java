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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.function.Supplier;

import static org.ameba.LoggingCategories.CALL_CONTEXT;

/**
 * A CallContextHolder provides access to the {@link CallContext} of the current request execution.
 *
 * @author Heiko Scherrer
 */
public final class CallContextHolder {

    private static final Logger EXC_LOGGER = LoggerFactory.getLogger(CallContextHolder.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(CALL_CONTEXT);
    private static final InheritableThreadLocal<CallContext> callContext = new InheritableThreadLocal<>();

    /**
     * Retrieve the current {@link CallContext}.
     *
     * @return The thread-bound instance
     */
    public static Optional<CallContext> getOptionalCallContext() {
        return Optional.ofNullable(callContext.get());
    }

    /**
     * Populate the {@link CallContext} with the callerId.
     *
     * @param caller The callerId
     */
    static void setCaller(Supplier<String> caller) {
        if (caller == null || caller.get() == null || caller.get().isEmpty()) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("CTXHolder: No caller to set in context");
            }
            return;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("CTXHolder: Populating context with caller [{}]", caller.get());
        }
        getOptionalCallContext().ifPresent(cc -> cc.setCaller(caller.get()));
    }

    /**
     * Get the {@link CallContext} as base64 encoded String.
     *
     * @return Base64 encoded String
     */
    public static Optional<String> getEncodedCallContext() {
        if (callContext.get() == null) {
            return Optional.empty();
        }
        try {
            var om = new ObjectMapper();
            return Optional.of(Base64.getEncoder().encodeToString(om.writeValueAsBytes(callContext.get())));
        } catch (JsonProcessingException e) {
            EXC_LOGGER.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    /**
     * Initialize the {@link CallContext} with the given base64 encoded {@code callContextString}.
     *
     * @param callContextString The base64 encoded CallContext as String
     * @param defaultCallContext The default CallContext used when no CallContext is provided by callContextString
     */
    public static void setCallContext(Supplier<String> callContextString, CallContext defaultCallContext) {
        if (callContextString == null || callContextString.get() == null || callContextString.get().isEmpty()) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("CTXHolder: Initialized CallContext [{}] with default", defaultCallContext);
            }
            callContext.set(defaultCallContext);
            return;
        }
        var binaryCallContext = Base64.getDecoder().decode(callContextString.get());
        try {
            var om = new ObjectMapper();
            var ctx = om.readValue(binaryCallContext, CallContext.class);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("CTXHolder: Decoded CallContext [{}]", ctx);
            }
            callContext.set(ctx);
        } catch (IOException e) {
            EXC_LOGGER.error("Decoded CallContext does not match the current CallContext version of the receiver. " + e.getMessage(), e);
        }
    }

    /**
     * Destroy the thread-bound {@link CallContext}.
     */
    static void destroy() {
        callContext.remove();
    }
}

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

import java.util.function.Supplier;

/**
 * A CallContextHolder provides access to the {@link CallContext} of the current request execution.
 *
 * @author Heiko Scherrer
 */
public final class CallContextHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallContextHolder.class);
    private static final InheritableThreadLocal<CallContext> callContext = new InheritableThreadLocal<>();

    public static CallContext getCallContext() {
        if (callContext.get() == null) {
            callContext.set(new CallContext());
        }
        return callContext.get();
    }

    public static void setCaller(Supplier<String> caller) {
        if (caller == null || caller.get() == null || caller.get().isEmpty()) {
            return;
        }
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Populating context with caller [{}]", caller.get());
        }
        getCallContext().setCaller(caller.get());
    }

    public static void destroy() {
        callContext.remove();
    }
}

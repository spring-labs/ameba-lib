/*
 * Copyright 2005-2023 the original author or authors.
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
package org.ameba.http.ctx.sleuth;

import org.ameba.http.ctx.CallContext;
import org.ameba.http.ctx.CallContextProvider;
import org.springframework.cloud.sleuth.Tracer;

/**
 * A TraceableCallContextInterceptor returns a {@link CallContext} instance that contains the current {@code traceId}.
 *
 * @author Heiko Scherrer
 */
class TraceableCallContextProvider implements CallContextProvider {

    private final transient Tracer tracer;

    public TraceableCallContextProvider(Tracer tracer) {
        this.tracer = tracer;
    }

    /**
     * {@inheritDoc}
     *
     * The traceId is included if a {@link org.springframework.cloud.sleuth.TraceContext TraceContext} is available.
     */
    @Override
    public CallContext getInitialCallContext() {
        var ctx = new CallContext();
        if (tracer != null && tracer.currentSpan() != null) {
            ctx.setTraceId(tracer.currentSpan().context().traceId());
        }
        return ctx;
    }
}

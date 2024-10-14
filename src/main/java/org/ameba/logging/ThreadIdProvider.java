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
package org.ameba.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A ThreadIdProvider tracks a sequence number that gets increased per created thread. This class is used to log an unique id per thread.
 *
 * Logback:
 * <pre>
 *     &lt;conversionRule conversionWord="tid" converterClass="org.ameba.logging.ThreadIdProvider" /&gt;
 *     &lt;appender&gt;
 *         &lt;encoder&gt;
 *             ...
 *             &lt;pattern&gt;%tid %m%n&lt;/pattern&gt;
 *         &lt;/encoder&gt;
 *     &lt;/appender&gt;
 * </pre>
 *
 * @author Heiko Scherrer
 */
public class ThreadIdProvider extends ClassicConverter {

    private final AtomicLong nextId = new AtomicLong();
    private final ThreadLocal<String> threadId = ThreadLocal.withInitial(() -> "" + nextId.incrementAndGet());

    /**
     * {@inheritDoc}
     */
    @Override
    public String convert(ILoggingEvent event) {
        return threadId.get();
    }
}
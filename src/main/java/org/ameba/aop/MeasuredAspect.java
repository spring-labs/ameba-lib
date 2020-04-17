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
package org.ameba.aop;

import org.ameba.LoggingCategories;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.util.StopWatch;

/**
 * An MeasuredAspect is used to measure execution time of methods in any application layer
 * annotated with {@link org.ameba.annotation.Measured Measured}. The order of the aspect
 * is {@literal 18}.
 *
 * @author Heiko Scherrer
 * @since 1.10
 */
@Aspect
@Order(18)
public class MeasuredAspect {

    /** Spring component name. */
    public static final String COMPONENT_NAME = "MeasuredAspect";
    private static final Logger MEASURED_LOGGER = LoggerFactory.getLogger(LoggingCategories.LOG_MEASURED);
    private static final Logger BOOT_LOGGER = LoggerFactory.getLogger(LoggingCategories.BOOT);

    /** Default constructor with some loginfo */
    public MeasuredAspect() {
        BOOT_LOGGER.info("-- w/ " + COMPONENT_NAME);
    }

    /**
     * The execution time is measured of {@link org.ameba.annotation.Measured Measured}
     * annotated,  Around intercepted methods when the log level of category `MEASURED` is
     * set to `INFO`.
     *
     * @param pjp The joinpoint
     * @return Method return value
     * @throws Throwable in case of errors
     */
    @Around("org.ameba.aop.Pointcuts.isMeasured()")
    public Object measure(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch sw = null;
        if (MEASURED_LOGGER.isInfoEnabled()) {
            sw = new StopWatch();
            sw.start();
            MEASURED_LOGGER.info("[TSL]>> {}#{}", pjp.getTarget().getClass().getSimpleName(), pjp.getSignature().getName());
        }
        try {
            return pjp.proceed();
        } finally {
            if (MEASURED_LOGGER.isInfoEnabled() && sw != null) {
                sw.stop();
                MEASURED_LOGGER.info("[TSL]<< {}#{} took {} [ms]", pjp.getTarget().getClass().getSimpleName(), pjp.getSignature().getName(), sw.getTotalTimeMillis());
            }
        }
    }
}

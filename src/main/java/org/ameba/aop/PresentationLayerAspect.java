/*
 * Copyright 2014-2015 the original author or authors.
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

/**
 * A PresentationLayerAspect.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 0.1
 * @since 1.2
 */
@Aspect
public class PresentationLayerAspect {

    private static final Logger EXC_LOGGER = LoggerFactory.getLogger(LoggingCategories.PRESENTATION_LAYER_EXCEPTION);
    private static final Logger BOOT_LOGGER = LoggerFactory.getLogger(LoggingCategories.BOOT);

    /**
     * Spring component name.
     */
    public static final String COMPONENT_NAME = "PresentationLayerAspect";

    /**
     * Create a new PresentationLayerAspect.
     */
    public PresentationLayerAspect() {
        BOOT_LOGGER.info("-- w/ " + COMPONENT_NAME);
    }

    /**
     * Called around method invocations to log exceptions occurred in intercepted layer.
     *
     * @param pjp the ProceedingJoinPoint object
     * @return the return value of the method invocation
     * @throws Throwable any exception thrown by the method invocation
     */
    @Around("org.ameba.aop.Pointcuts.presentationPointcut()")
    public Object measure(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();
        } catch (Exception ex) {
            EXC_LOGGER.error(ex.getMessage(), ex);
            throw ex;
        }
    }
}

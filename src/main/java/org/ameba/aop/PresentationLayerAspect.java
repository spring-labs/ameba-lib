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
package org.ameba.aop;

import org.ameba.LoggingCategories;
import org.ameba.annotation.NotLogged;
import org.ameba.exception.BusinessRuntimeException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

/**
 * A PresentationLayerAspect defines the intercepting code around the presentation layer, basically around Spring MVC controllers. The
 * order of the aspect is {@literal 20}.
 *
 * @author Heiko Scherrer
 */
@Aspect
@Order(20)
public class PresentationLayerAspect {

    /** Spring component name. */
    public static final String COMPONENT_NAME = "PresentationLayerAspect";
    private static final Logger EXC_LOGGER = LoggerFactory.getLogger(LoggingCategories.PRESENTATION_LAYER_EXCEPTION);
    private static final Logger BOOT_LOGGER = LoggerFactory.getLogger(LoggingCategories.BOOT);

    /** Default constructor with some loginfo */
    public PresentationLayerAspect() {
        BOOT_LOGGER.info("-- w/ " + COMPONENT_NAME);
    }

    /**
     * Logging and exception translation happens for intercepted methods.
     * <ul>
     *     <li>Set log level of {@link LoggingCategories#PRESENTATION_LAYER_EXCEPTION} to ERROR to enable exception logging.</li>
     * </ul>
     *
     * @param pjp The joinpoint
     * @return Method return value
     * @throws Throwable in case of errors
     */
    @Around("org.ameba.aop.Pointcuts.presentationPointcut()")
    public Object measure(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();
        } catch (Exception ex) {
            if (ex instanceof BusinessRuntimeException bre) {
                if (EXC_LOGGER.isErrorEnabled() && ex.getClass().getAnnotation(NotLogged.class) != null) {
                    EXC_LOGGER.error(ex.getMessage(), ex, bre.getData());
                }
            } else {
                if (EXC_LOGGER.isErrorEnabled() && ex.getClass().getAnnotation(NotLogged.class) != null) {
                    EXC_LOGGER.error(ex.getMessage(), ex);
                }
            }
            throw ex;
        }
    }
}

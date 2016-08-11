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

import java.util.Optional;

import org.ameba.LoggingCategories;
import org.ameba.exception.BusinessRuntimeException;
import org.ameba.exception.ServiceLayerException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

/**
 * A ServiceLayerAspect is spawned around all public API methods and is responsible to log method execution time and log occurring
 * exceptions around the service layer.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 0.1
 * @since 1.2
 */
@Aspect
@Order(15)
public class ServiceLayerAspect {

    /** Springs component name. */
    public static final String COMPONENT_NAME = "ServiceLayerAspect";
    private static final Logger SRV_LOGGER = LoggerFactory.getLogger(LoggingCategories.SERVICE_LAYER_ACCESS);
    private static final Logger EXC_LOGGER = LoggerFactory.getLogger(LoggingCategories.SERVICE_LAYER_EXCEPTION);
    private static final Logger BOOT_LOGGER = LoggerFactory.getLogger(LoggingCategories.BOOT);

    /** Default constructor with some loginfo */
    public ServiceLayerAspect() {
        BOOT_LOGGER.info("-- w/ " + COMPONENT_NAME);
    }

    /**
     * Around intercepted methods do some logging and exception translation. <p> <ul> <li> Set log level of {@link
     * LoggingCategories#SERVICE_LAYER_ACCESS} to INFO to enable method tracing. <li>Set log level of {@link
     * LoggingCategories#SERVICE_LAYER_EXCEPTION} to ERROR to enable exception logging. </ul> </p>
     *
     * @param pjp The joinpoint
     * @return Method return value
     * @throws Throwable in case of errors
     */
    @Around("org.ameba.aop.Pointcuts.servicePointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long startMillis = 0L;
        if (SRV_LOGGER.isDebugEnabled()) {
            SRV_LOGGER.debug("[S]>> Method call: " + pjp.toShortString());
            startMillis = System.currentTimeMillis();
        }

        Object obj = null;
        try {
            obj = pjp.proceed();
        } catch (Exception ex) {
            throw translateException(ex);
        } finally {
            if (SRV_LOGGER.isDebugEnabled()) {
                SRV_LOGGER.debug("[S]<< " + pjp.toShortString() + " took [ms]: " + (System.currentTimeMillis() - startMillis));
            }
        }
        return obj;
    }

    /**
     * Called after an exception is thrown by classes of the service layer. <p> Set log level to ERROR to log the root cause. </p>
     *
     * @param ex The root exception that is thrown
     * @return Returns the exception to be thrown
     */
    public Exception translateException(Exception ex) {
        if (EXC_LOGGER.isErrorEnabled()) {
            EXC_LOGGER.error("[S] Service Layer Exception: " + ex.getLocalizedMessage(), ex);
        }

        if (ex instanceof BusinessRuntimeException) {
            return ex;
        }

        Optional<Exception> handledException = doTranslateException(ex);
        if (handledException.isPresent()) {
            return handledException.get();
        }
        if (ex instanceof ServiceLayerException) {
            return ex;
        }
        return new ServiceLayerException(ex.getMessage());
    }

    /**
     * Override method to handle the transaction yourself and skip to the default exception handling .
     *
     * @param ex Exception to handle
     * @return An empty Optional to use the default exception handling or an Exception to skip default handling
     */
    protected Optional<Exception> doTranslateException(Exception ex) {
        return Optional.empty();
    }
}

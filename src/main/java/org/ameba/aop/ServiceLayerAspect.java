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
import org.ameba.annotation.NotTransformed;
import org.ameba.exception.BusinessRuntimeException;
import org.ameba.exception.ServiceLayerException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A ServiceLayerAspect is spawned around the business service layer and is responsible to log method execution time and occurring
 * exceptions. The order of the aspect is {@literal 15}.
 *
 * @author Heiko Scherrer
 */
@Aspect
@Order(15)
public class ServiceLayerAspect {

    /** Springs component name. */
    public static final String COMPONENT_NAME = "ServiceLayerAspect";
    private static final Logger SRV_LOGGER = LoggerFactory.getLogger(LoggingCategories.SERVICE_LAYER_ACCESS);
    private static final Logger EXC_LOGGER = LoggerFactory.getLogger(LoggingCategories.SERVICE_LAYER_EXCEPTION);
    private static final Logger BOOT_LOGGER = LoggerFactory.getLogger(LoggingCategories.BOOT);
    private boolean withRootCause = false;
    private ExceptionTranslator exceptionTranslator;

    /** Default constructor with some loginfo. */
    public ServiceLayerAspect() {
        BOOT_LOGGER.info("-- w/ {}", COMPONENT_NAME);
    }

    /**
     * Constructor with some loginfo and considering the root cause.
     *
     * @param withRootCause Whether the root cause shall be preserved
     * @param exceptionTranslator An instance used to translate exceptions
     */
    public ServiceLayerAspect(boolean withRootCause, ExceptionTranslator exceptionTranslator) {
        BOOT_LOGGER.info("-- w/ {}", COMPONENT_NAME);
        this.withRootCause = withRootCause;
        this.exceptionTranslator = exceptionTranslator;
    }

    /**
     * Logging and exception translation happens for intercepted methods.
     * <ul>
     *     <li>Set log level of {@link LoggingCategories#SERVICE_LAYER_ACCESS} to INFO to enable method tracing.</li>
     *     <li>Set log level of {@link LoggingCategories#SERVICE_LAYER_EXCEPTION} to ERROR to enable exception logging.</li>
     * </ul>
     *
     * @param pjp The joinpoint
     * @return Method return value
     * @throws Throwable in case of errors
     */
    @Around("org.ameba.aop.Pointcuts.servicePointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long startMillis = 0L;
        if (SRV_LOGGER.isInfoEnabled()) {
            SRV_LOGGER.info("[S]>> Method call: {}", pjp.toShortString());
            startMillis = System.currentTimeMillis();
        }

        Object obj;
        try {
            obj = pjp.proceed();
        } catch (Exception ex) {
            var method = ((MethodSignature) pjp.getSignature()).getMethod();
            var notTransformed = method.getAnnotation(NotTransformed.class);
            if (notTransformed == null) {
                Exception e = translateException(ex);
                if (EXC_LOGGER.isErrorEnabled() && !hasNotLogged(ex)) {
                    EXC_LOGGER.error(e.getLocalizedMessage(), e);
                }
                throw e;
            }
            throw ex;
        } finally {
            if (SRV_LOGGER.isInfoEnabled()) {
                SRV_LOGGER.info("[S]<< {} took [{}] (ms)", pjp.toShortString(), (System.currentTimeMillis() - startMillis));
            }
        }
        return obj;
    }

    private boolean hasNotLogged(Throwable ex) {
        if (ex.getClass().getAnnotation(NotLogged.class) != null) {
            return true;
        }
        if (ex.getCause() != null) {
            return hasNotLogged(ex.getCause());
        }
        return false;
    }

    /**
     * Called after an exception is thrown by classes of the service layer. <p> Set log level to ERROR to log the root cause. </p>
     *
     * @param ex The root exception that is thrown
     * @return Returns the exception to be thrown
     */
    public Exception translateException(Exception ex) {
        if (ex instanceof BusinessRuntimeException bre) {
            MDC.put(LoggingCategories.MSGKEY, bre.getMessageKey());
            if (bre.getData() != null) {
                MDC.put(LoggingCategories.MSGDATA, String.join(",", Stream.of(bre.getData()).filter(Objects::nonNull).map(Object::toString).toArray(String[]::new)));
            }
            // cleanup of context is done in SLF4JMappedDiagnosticContextFilter
            return bre;
        }

        Optional<Exception> handledException = doTranslateException(ex);
        if (handledException.isPresent()) {
            return handledException.get();
        }
        if (ex instanceof ServiceLayerException) {
            return ex;
        }
        return withRootCause ? new ServiceLayerException(ex.getMessage(), ex) : new ServiceLayerException(ex.getMessage());
    }

    /**
     * Override method to handle the transaction yourself and skip to the default exception handling .
     *
     * @param ex Exception to handle
     * @return An empty Optional to use the default exception handling or an Exception to skip default handling
     */
    protected Optional<Exception> doTranslateException(Exception ex) {
        if (exceptionTranslator == null) {
            return Optional.empty();
        }
        return exceptionTranslator.translate(ex);
    }
}

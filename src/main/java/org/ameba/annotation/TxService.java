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
package org.ameba.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A TxService is a stereotype annotation to define a transactional Spring managed service.
 *
 * @author Heiko Scherrer
 */
@Validated
@Transactional
@Service
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TxService {

    /**
     * {@inheritDoc}
     *
     * @see Component#value()
     */
    @AliasFor(annotation = Component.class, attribute = "value") String value() default "";

    /**
     * {@inheritDoc}
     *
     * @see Transactional#transactionManager()
     */
    @AliasFor(annotation = Transactional.class, attribute = "transactionManager") String transactionManager() default "";

    /**
     * {@inheritDoc}
     *
     * @see Transactional#propagation()
     */
    @AliasFor(annotation = Transactional.class, attribute = "propagation") Propagation propagation() default Propagation.REQUIRED;

    /**
     * {@inheritDoc}
     *
     * @see Transactional#readOnly()
     */
    @AliasFor(annotation = Transactional.class, attribute = "readOnly") boolean readOnly() default false;

    /**
     * {@inheritDoc}
     *
     * @see Transactional#timeout()
     */
    @AliasFor(annotation = Transactional.class, attribute = "timeout") int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;

    /**
     * {@inheritDoc}
     *
     * @see Transactional#rollbackFor()
     */
    @AliasFor(annotation = Transactional.class, attribute = "rollbackFor") Class<? extends Throwable>[] rollbackFor() default {};

    /**
     * {@inheritDoc}
     *
     * @see Transactional#isolation()
     */
    @AliasFor(annotation = Transactional.class, attribute = "isolation") Isolation isolation() default Isolation.DEFAULT;
}

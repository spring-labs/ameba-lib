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
package org.ameba.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * A TxService is a stereotype annotation to define transactional Spring managed services.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.1
 * @since 1.0
 */
@Transactional
@Service
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TxService {

    /**
     * @see Component#value()
     */
    @AliasFor(annotation = Component.class, attribute = "value")
    String value() default "";

    /**
     * @see Transactional#transactionManager()
     */
    @AliasFor(annotation = Transactional.class, attribute = "transactionManager")
    String transactionManager() default "";

    /**
     * @see Transactional#propagation()
     */
    @AliasFor(annotation = Transactional.class, attribute = "propagation")
    Propagation propagation() default Propagation.REQUIRED;

    /**
     * @see Transactional#readOnly()
     */
    @AliasFor(annotation = Transactional.class, attribute = "readOnly")
    boolean readOnly() default false;

    /**
     * @see Transactional#timeout()
     */
    @AliasFor(annotation = Transactional.class, attribute = "timeout")
    int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;

    /**
     * @see Transactional#rollbackFor()
     */
    @AliasFor(annotation = Transactional.class, attribute = "rollbackFor")
    Class<? extends Throwable>[] rollbackFor() default {};

    /**
     * @see Transactional#isolation()
     */
    @AliasFor(annotation = Transactional.class, attribute = "isolation")
    Isolation isolation() default Isolation.DEFAULT;

}

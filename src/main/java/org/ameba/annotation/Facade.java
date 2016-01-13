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
 * A Facade is a stereotype annotation that marks a public service as a facade, that is able to delegate to multiple
 * services, whereas each service is responsible for its own business domain. The facade is a Spring managed
 * transactional service bean.
 * The concept is similar to the J2EE Session Facade pattern.
 * Also see J2EE Service Facade pattern http://corej2eepatterns.com/SessionFacade.htm
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.1
 * @since 1.0
 * @deprecated Use a {@link TxService @TxService} in addition.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Transactional
@Service
@Deprecated
public @interface Facade {

    /**
     * @see Component#value()
     */
    @AliasFor(annotation = Component.class, attribute = "value") String value() default "";

    /**
     * @see Transactional#transactionManager()
     * @deprecated Use a {@link TxService @TxService} instead.
     */
    @Deprecated
    @AliasFor(annotation = Transactional.class, attribute = "transactionManager") String transactionManager() default "";

    /**
     * @see Transactional#propagation()
     * @deprecated Use a {@link TxService @TxService} instead.
     */
    @Deprecated
    @AliasFor(annotation = Transactional.class, attribute = "propagation") Propagation propagation() default Propagation.REQUIRED;

    /**
     * @see Transactional#readOnly()
     * @deprecated Use a {@link TxService @TxService} instead.
     */
    @Deprecated
    @AliasFor(annotation = Transactional.class, attribute = "readOnly") boolean readOnly() default false;

    /**
     * @see Transactional#timeout()
     * @deprecated Use a {@link TxService @TxService} instead.
     */
    @Deprecated
    @AliasFor(annotation = Transactional.class, attribute = "timeout") int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;

    /**
     * @see Transactional#rollbackFor()
     * @deprecated Use a {@link TxService @TxService} instead.
     */
    @Deprecated
    @AliasFor(annotation = Transactional.class, attribute = "rollbackFor") Class<? extends Throwable>[] rollbackFor() default {};

    /**
     * @see Transactional#isolation()
     * @deprecated Use a {@link TxService @TxService} instead.
     */
    @Deprecated
    @AliasFor(annotation = Transactional.class, attribute = "isolation") Isolation isolation() default Isolation.DEFAULT;
}

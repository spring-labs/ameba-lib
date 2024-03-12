/*
 * Copyright 2015-2023 the original author or authors.
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

import org.aspectj.lang.annotation.Pointcut;

/**
 * A Pointcuts class encapsulates AOP pointcut definitions that are specific to the application architecture. Because pointcuts can not be
 * extended, they must be patched in projects by putting the same class on the projects classpath.
 *
 * @author Heiko Scherrer
 */
public class Pointcuts extends SpringPointcuts {

    /** Override this method with your custom pointcut definition. */
    @Pointcut("isRestController()")
    public void presentationPointcut() {
        // intended to kept empty because it is just the definition of the AOP pointcut
    }

    /** Override this method with your custom pointcut definition. */
    @Pointcut("isService() || isTransactionalService()")
    public void servicePointcut() {
        // intended to kept empty because it is just the definition of the AOP pointcut
    }

    /** Override this method with your custom pointcut definition. */
    @Pointcut("isRepository()")
    public void integrationPointcut() {
        // intended to kept empty because it is just the definition of the AOP pointcut
    }

    /** Map classes with Validated. */
    @Pointcut("@within(org.springframework.validation.annotation.Validated)")
    public void isValidated() {
        // intended to kept empty because it is just the definition of the AOP pointcut
    }

    /** Map methods with Measured. */
    @Pointcut("@within(org.ameba.http.MeasuredRestController) || @annotation(org.ameba.annotation.Measured)")
    public void isMeasured() {
        // intended to kept empty because it is just the definition of the AOP pointcut
    }
}

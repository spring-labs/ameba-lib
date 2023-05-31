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
package org.ameba.annotation;

import org.springframework.context.annotation.AspectsSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An EnableAspects annotation is used to include Ameba aspects for exception translation, logging and method execution consumption
 * logging.
 *
 * @author Heiko Scherrer
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AspectsSelector.class)
public @interface EnableAspects {

    /**
     * Switch to enable root cause propagation. Default is {@literal true}.
     * <p>
     * Usually root cause exceptions are omitted when translating them. This switch enables to pass the root cause (and the exception stack)
     * to the caller.
     *
     * @return Whether exception root causes shall be propagated and logged or not
     */
    boolean propagateRootCause() default false;
}

/*
 * Copyright 2005-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ameba.aop;

import org.ameba.annotation.NotLogged;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A AspectSupportTest pins down the semantics of {@link AspectSupport#hasNotLogged(Throwable)}.
 *
 * @author Heiko Scherrer
 */
class AspectSupportTest {

    @Test void returnsFalseForUnannotatedException() {
        assertThat(AspectSupport.hasNotLogged(new RuntimeException("plain"))).isFalse();
    }

    @Test void returnsTrueForDirectlyAnnotatedException() {
        assertThat(AspectSupport.hasNotLogged(new AnnotatedException())).isTrue();
    }

    @Test void returnsTrueForAnnotationOnCauseAtDepth1() {
        var wrapped = new RuntimeException("outer", new AnnotatedException());
        assertThat(AspectSupport.hasNotLogged(wrapped)).isTrue();
    }

    @Test void returnsTrueForAnnotationDeepInCauseChain() {
        var wrapped = new RuntimeException("outer",
                new RuntimeException("middle",
                        new RuntimeException("inner", new AnnotatedException())));
        assertThat(AspectSupport.hasNotLogged(wrapped)).isTrue();
    }

    @Test void returnsTrueForSubclassOfAnnotatedBaseDueToInherited() {
        assertThat(AspectSupport.hasNotLogged(new SubclassException())).isTrue();
    }

    @NotLogged
    static class AnnotatedException extends RuntimeException {
        AnnotatedException() { super("annotated"); }
    }

    @NotLogged
    static class AnnotatedBaseException extends RuntimeException {
        AnnotatedBaseException(String m) { super(m); }
    }

    static class SubclassException extends AnnotatedBaseException {
        SubclassException() { super("sub"); }
    }
}

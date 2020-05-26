/*
 * Copyright 2005-2020 the original author or authors.
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
package org.ameba.http;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An EnableIdentityAwareness stereotyped application expects to retrieve and work with representations of human identities.
 *
 * @author Heiko Scherrer
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableIdentityAwareness {

    /**
     * Turn Access Protection by Identity on or off. Default is {@literal true}.
     *
     * @return Set to {@literal false}, to disable
     */
    boolean enabled() default true;

    /**
     * Define whether an exception should be thrown when Access Protection by Identity is enabled but no identity information is provided.
     * Default is {@literal false}.
     *
     * @return Set to {@literal true} to throw exception if not present
     */
    boolean throwIfNotPresent() default false;

    /**
     * All patterns of protected URL.
     *
     * @return Patterns matching the URL to activate Access Protection by Identity on (Ant style not supported)
     */
    String[] urlPatterns() default "/*";

    /**
     * Implementation class used to resolve the current identity.
     *
     * @return implementation class;
     */
    Class<? extends IdentityResolverStrategy> identityResolverStrategy() default HeaderAttributeResolverStrategy.class;
}

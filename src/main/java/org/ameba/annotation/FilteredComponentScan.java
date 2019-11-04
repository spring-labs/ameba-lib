/*
 * Copyright 2014-2018 the original author or authors.
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

import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ScopeMetadataResolver;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A FilteredComponentScan is an extension to Spring's {@link ComponentScan ComponentScan} annotation, in that it uses a preset default
 * exclusion filter, for types annotated with {@link ExcludeFromScan ExcludeFromScan}.
 *
 * @author Heiko Scherrer
 * @version 1.0
 * @since 1.4
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ComponentScan
public @interface FilteredComponentScan {

    /**
     * {@inheritDoc}
     */
    @AliasFor("basePackages") String[] value() default {};

    /**
     * {@inheritDoc}
     */
    @AliasFor("value") String[] basePackages() default {};

    /**
     * {@inheritDoc}
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * {@inheritDoc}
     */
    Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

    /**
     * {@inheritDoc}
     */
    Class<? extends ScopeMetadataResolver> scopeResolver() default AnnotationScopeMetadataResolver.class;

    /**
     * {@inheritDoc}
     */
    ScopedProxyMode scopedProxy() default ScopedProxyMode.DEFAULT;

    /**
     * {@inheritDoc}
     */
    String resourcePattern() default "**/*.class";

    /**
     * {@inheritDoc}
     */
    boolean useDefaultFilters() default true;

    /**
     * {@inheritDoc}
     */
    ComponentScan.Filter[] includeFilters() default {};

    /**
     * {@inheritDoc}
     */
    ComponentScan.Filter[] excludeFilters() default {@ComponentScan.Filter(ExcludeFromScan.class)};

    /**
     * {@inheritDoc}
     */
    boolean lazyInit() default false;
}

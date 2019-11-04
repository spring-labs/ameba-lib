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
package org.ameba.http;

import org.ameba.integration.hibernate.TenantResolverTenancyStrategy;
import org.ameba.integration.jpa.SeparationStrategy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.MultiTenancySelector;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A EnableMultiTenancy is able to enable Spring configuration for multi-tenancy.
 *
 * @author Heiko Scherrer
 * @since 1.7
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MultiTenancySelector.class)
public @interface EnableMultiTenancy {

    public static final String DEFAULT_SCHEMA = "public";

    /**
     * Turn multi-tenancy support on or off. Default is {@literal true}.
     *
     * @return Set to {@literal false}, to disable
     */
    boolean enabled() default true;

    /**
     * Define whether an exception should be thrown when multi-tenancy is enabled but no tenant information is available. Default is
     * {@literal false}.
     *
     * @return Set to {@literal true} to throw exception if not present
     */
    boolean throwIfNotPresent() default false;

    /**
     * All url patterns that should be handled by the multi-tenancy filters.
     *
     * @return Patterns matching the URL to activate multi-tenancy on, no Ant style supported
     */
    String[] urlPatterns() default "/*";

    /**
     * The strategy used to separate tenant data in databases. Default is {@link SeparationStrategy#NONE}
     *
     * @return The strategy to use
     */
    SeparationStrategy separationStrategy() default SeparationStrategy.NONE;

    /**
     * Implementation class used to resolve the current tenant from. In case of Hibernate, the class must implement
     * {@code CurrentTenantIdentifierResolver}.
     *
     * @return implementation class;
     */
    Class tenantResolverStrategy() default TenantResolverTenancyStrategy.class;

    /**
     * Define the name of the default database schema that is used when no tenant information is available. Default is
     * {@value DEFAULT_SCHEMA}.
     *
     * @return Name of the default schema
     */
    String defaultDatabaseSchema() default DEFAULT_SCHEMA;
}

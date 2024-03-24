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
package org.springframework.context.annotation;

import org.ameba.http.multitenancy.MultiTenancyConfiguration;
import org.ameba.integration.EnableMultiTenancy;
import org.ameba.integration.SeparationStrategy;
import org.ameba.integration.hibernate.DefaultMultiTenantConnectionProvider;
import org.ameba.integration.hibernate.SchemaBasedTenancyConfiguration;
import org.ameba.integration.hibernate.SchemaSeparationConfigurator;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Constructor;

/**
 * A MultiTenancySelector does the programmatic configuration based on the {@link EnableMultiTenancy} counterpart.
 *
 * @author Heiko Scherrer
 * @since 1.7
 */
public class MultiTenancySelector implements ImportSelector {

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Class<?> annoType = EnableMultiTenancy.class;
        AnnotationAttributes attributes = AnnotationConfigUtils.attributesFor(importingClassMetadata, annoType);
        if (attributes.getBoolean("enabled")) {
            MultiTenancyConfiguration.urlPatterns = attributes.getStringArray("urlPatterns");
            MultiTenancyConfiguration.enabled = attributes.getBoolean("enabled");
            MultiTenancyConfiguration.throwIfNotPresent = attributes.getBoolean("throwIfNotPresent");
            try {
                Class<?> resolverStrategyClass = attributes.getClass("tenantResolverStrategy");
                Constructor<?> ctor = resolverStrategyClass.getConstructor(String.class);
                SchemaSeparationConfigurator.tenantResolver = ctor.newInstance(attributes.getString("defaultDatabaseSchema"));
            } catch (Exception e) {
                throw new ApplicationContextException("Cannot instantiate a TenantResolverStrategy class to support multi-tenancy, please check @EnableMutliTenancy", e);
            }
            DefaultMultiTenantConnectionProvider.defaultSchema = attributes.getString("defaultDatabaseSchema");
            return attributes.getEnum("separationStrategy").equals(SeparationStrategy.SCHEMA) ?
                    new String[]{MultiTenancyConfiguration.class.getName(), SchemaBasedTenancyConfiguration.class.getName()} :
                    new String[]{MultiTenancyConfiguration.class.getName()};
        }
        return new String[]{};
    }
}

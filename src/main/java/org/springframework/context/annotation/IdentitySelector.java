/*
 * Copyright 2015-2020 the original author or authors.
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

import org.ameba.http.identity.EnableIdentityAwareness;
import org.ameba.http.identity.IdentityConfiguration;
import org.ameba.http.identity.amqp.IdentityFeignConfiguration;
import org.ameba.http.identity.IdentityResolverStrategy;
import org.ameba.http.identity.amqp.IdentityAmqpConfiguration;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Constructor;

/**
 * A IdentitySelector does the programmatic configuration based on the {@link org.ameba.http.identity.EnableIdentityAwareness} counterpart.
 *
 * @author Heiko Scherrer
 */
public class IdentitySelector implements ImportSelector {

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Class<?> annoType = EnableIdentityAwareness.class;
        AnnotationAttributes attributes = AnnotationConfigUtils.attributesFor(importingClassMetadata, annoType);
        if (attributes.getBoolean("enabled")) {
            IdentityConfiguration.urlPatterns = attributes.getStringArray("urlPatterns");
            IdentityConfiguration.enabled = attributes.getBoolean("enabled");
            IdentityConfiguration.throwIfNotPresent = attributes.getBoolean("throwIfNotPresent");
            try {
                Class<? extends IdentityResolverStrategy> resolverStrategyClass = attributes.getClass("identityResolverStrategy");
                Constructor<? extends IdentityResolverStrategy> ctor = resolverStrategyClass.getConstructor();
                IdentityConfiguration.strategy = ctor.newInstance();
            } catch (Exception e) {
                throw new ApplicationContextException("Cannot instantiate a IdentityResolverStrategy class to support identity resolution, please check @EnableIdentityAwareness", e);
            }
            return new String[]{IdentityConfiguration.class.getName(), IdentityAmqpConfiguration.class.getName(), IdentityFeignConfiguration.class.getName()};
        }
        return new String[]{};
    }
}

/*
 * Copyright 2015-2024 the original author or authors.
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

import org.ameba.annotation.EnableAspects;
import org.ameba.aop.AspectsConfiguration;
import org.ameba.integration.EnableMultiTenancy;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * A AspectsSelector does the programatic configuration based on the {@link EnableMultiTenancy} counterpart.
 *
 * @author Heiko Scherrer
 * @since 1.7
 */
public class AspectsSelector implements ImportSelector {

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Class<?> annoType = EnableAspects.class;
        AnnotationAttributes attributes = AnnotationConfigUtils.attributesFor(importingClassMetadata, annoType);
        if (attributes == null) {
            throw new IllegalArgumentException(String.format(
                    "@%s is not present on importing class '%s' as expected",
                    annoType.getSimpleName(), importingClassMetadata.getClassName()));
        }
        AspectsConfiguration.withRootCause = attributes.getBoolean("propagateRootCause");
        return new String[]{AspectsConfiguration.class.getName()};
    }
}

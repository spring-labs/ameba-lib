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
package org.ameba.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * A NestedReloadableResourceBundleMessageSource.
 *
 * @author Heiko Scherrer
 */
public class NestedReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(NestedReloadableResourceBundleMessageSource.class);
    private static final String PROPERTIES_SUFFIX = ".properties";
    private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    /**
     * Create a new instance without any {@code basenames} set.
     */
    public NestedReloadableResourceBundleMessageSource() {
        super();
    }

    /**
     * Create a new instance with {@code basenames} to {@code MessageSource} implementations.
     *
     * @param basenames Really the basenames to properties files without the .properties extension
     */
    public NestedReloadableResourceBundleMessageSource(String... basenames) {
        super();
        this.setBasenames(basenames);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Consider {@code classpath*:} prefix to search for messageSources within jar files.
     */
    @Override
    protected PropertiesHolder refreshProperties(String filename, PropertiesHolder propHolder) {
        if (filename.startsWith(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX)) {
            return refreshClassPathProperties(filename, propHolder);
        } else {
            return super.refreshProperties(filename, propHolder);
        }
    }

    private PropertiesHolder refreshClassPathProperties(String filename, PropertiesHolder propHolder) {
        var properties = new Properties();
        long lastModified = -1;
        try {
            var resources = resolver.getResources(filename + PROPERTIES_SUFFIX);
            for (var resource : resources) {
                var sourcePath = resource.getURI().toString().replace(PROPERTIES_SUFFIX, "");
                var holder = super.refreshProperties(sourcePath, propHolder);
                properties.putAll(holder.getProperties());
                if (lastModified < resource.lastModified())
                    lastModified = resource.lastModified();
            }
        } catch (IOException ioe) {
            LOGGER.error("Failed to refresh classpath properties", ioe);
        }
        return new PropertiesHolder(properties, lastModified);
    }
}

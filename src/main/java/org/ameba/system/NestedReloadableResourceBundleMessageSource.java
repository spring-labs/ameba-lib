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
package org.ameba.system;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * A NestedReloadableResourceBundleMessageSource.
 *
 * @author Heiko Scherrer
 */
public class NestedReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {

    private static final String PROPERTIES_SUFFIX = ".properties";
    private PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    /**
     * Create a new instance without any {@code basenames} set.
     */
    public NestedReloadableResourceBundleMessageSource() {
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
        if (filename.startsWith(PathMatchingResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX)) {
            return refreshClassPathProperties(filename, propHolder);
        } else {
            return super.refreshProperties(filename, propHolder);
        }
    }

    private PropertiesHolder refreshClassPathProperties(String filename, PropertiesHolder propHolder) {
        Properties properties = new Properties();
        long lastModified = -1;
        try {
            Resource[] resources = resolver.getResources(filename + PROPERTIES_SUFFIX);
            for (Resource resource : resources) {
                String sourcePath = resource.getURI().toString().replace(PROPERTIES_SUFFIX, "");
                PropertiesHolder holder = super.refreshProperties(sourcePath, propHolder);
                properties.putAll(holder.getProperties());
                if (lastModified < resource.lastModified())
                    lastModified = resource.lastModified();
            }
        } catch (IOException ignored) {
        }
        return new PropertiesHolder(properties, lastModified);
    }
}

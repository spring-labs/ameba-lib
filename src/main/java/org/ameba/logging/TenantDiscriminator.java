/*
 * Copyright 2014-2019 the original author or authors.
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
package org.ameba.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.Discriminator;
import org.ameba.tenancy.TenantHolder;

import static org.ameba.LoggingCategories.BOOT;

/**
 * A TenantDiscriminator is a Logback extension to resolve the current Tenant and integrate it into Logback ecosystem.
 *
 * @author Heiko Scherrer
 */
public class TenantDiscriminator implements Discriminator<ILoggingEvent> {

    private boolean started;

    @Override
    public String getDiscriminatingValue(ILoggingEvent iLoggingEvent) {
        return TenantHolder.getCurrentTenant() == null ? BOOT : TenantHolder.getCurrentTenant();
    }

    @Override
    public String getKey() {
        return "Tenant";
    }

    @Override
    public void start() {
        started = true;
    }

    @Override
    public void stop() {
        started = false;
    }

    @Override
    public boolean isStarted() {
        return started;
    }
}

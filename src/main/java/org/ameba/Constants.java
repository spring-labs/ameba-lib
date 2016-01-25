/*
 * Copyright 2014-2015 the original author or authors.
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
package org.ameba;

/**
 * A Constants class to collect common used constants.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.1
 * @since 0.9
 */
public final class Constants {

    /**
     * Name of http header attribute used to carry tenant identifier from client to server.
     */
    public static final String HEADER_VALUE_TENANT = "Tenant";
    /**
     * Name of http X- header attribute used to carry tenant identifier from client to server.
     */
    public static final String HEADER_VALUE_X_TENANT = "X-Tenant";
    /**
     * Name of servlet context param to enable multitenancy support, expected to be set as {@code true} or {@code false}
     * .
     */
    public static final String PARAM_MULTI_TENANCY_ENABLED = "multitenancy.enabled";
    /**
     * Name of servlet context param to define whether an exception shall be thrown in case the tenant identifier is not
     * present in the http header.
     */
    public static final String PARAM_MULTI_TENANCY_THROW_IF_NOT_PRESENT = "multitenancy.throw";

    /**
     * Hide constructor.
     */
    private Constants() {
    }
}

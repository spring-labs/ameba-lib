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
package org.ameba;

/**
 * A Constants class to collect common used constants.
 *
 * @author Heiko Scherrer
 * @since 0.9
 */
public final class Constants {

    /**
     * Name of http X- header attribute used to carry tenant identifier from client to server.
     */
    public static final String HEADER_VALUE_X_TENANT = "X-Tenant";
    /**
     * Name of servlet context param to enable multitenancy support, expected to be set as {@code true} or {@code
     * false}.
     */
    public static final String PARAM_MULTI_TENANCY_ENABLED = "multitenancy.enabled";
    /**
     * Name of servlet context param to define whether an exception shall be thrown in case the tenant identifier is not
     * present in the http header.
     */
    public static final String PARAM_MULTI_TENANCY_THROW_IF_NOT_PRESENT = "multitenancy.throw";
    /**
     * Name of the custome http X- attribute used to store an unique request ID.
     */
    public static final String HEADER_VALUE_X_REQUESTID = "X-RequestID";
    /**
     * Standard date time format for APIs.
     */
    public static final String API_DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
    /**
     * Standard date time format for APIs.
     */
    public static final String API_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * Standard date time format for APIs.
     */
    public static final String API_TIME_FORMAT = "hh:mm:ss";

    /**
     * Hide constructor.
     */
    private Constants() {
    }
}

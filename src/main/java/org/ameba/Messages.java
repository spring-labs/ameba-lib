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
package org.ameba;

/**
 * A Messages contains all possible message and problem keys.
 *
 * @author Heiko Scherrer
 */
public final class Messages {

    /*~ Server status */
    /** Expresses a generic server-okay status. */
    public static final String SERVER_OK = "server.ok";
    /** Expresses a generic server-not-okay status. */
    public static final String SERVER_NOK = "server.nok";

    /*~ Security */
    /** Signals bad credentials during authentication. */
    public static final String BAD_CREDENTIALS = "server.bad.credentials";
    /** Expresses a successful authentication. */
    public static final String AUTHENTICATED = "server.authenticated";
    /** Expresses a unsuccessful authentication. */
    public static final String UNAUTHENTICATED = "server.unauthenticated";
    /** Not allowed to access a resource. */
    public static final String UNAUTHORIZED = "server.unauthorized";
    /** Signals that a logout attempt has failed. */
    public static final String LOGOUT_FAILED = "server.logout.failed";

    /*~ Generic */
    /** Signals that a resource has been created successfully. */
    public static final String CREATED = "generic.created";
    /** Signals that a requested resource does not exist. */
    public static final String NOT_FOUND = "not.found";
    /** Signals that a resource does exist unexpectedly. */
    public static final String ALREADY_EXISTS = "already.exists";
    /** Signals a generic gateway problem. */
    public static final String GW_GENERIC = "gw.generic";
    /** Signals a gateway timeout. */
    public static final String GW_TIMEOUT = "gw.timeout";
    /** Signals a bad request. */
    public static final String REQ_GENERIC = "req.generic-error";
	/** Signals that a resource has changed unexpectedly and cannot be modified by the caller. */
	public static final String RESOURCE_CHANGED_UNEXPECTEDLY = "resource.changed.unexpectedly";

    private Messages() { }
}

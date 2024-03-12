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
package org.ameba.http;

/**
 * A RequestHolder stores a requestID inside an inheritable thread local variable.
 *
 * @author Heiko Scherrer
 */
public final class RequestIDHolder {

    private static final InheritableThreadLocal<String> holder = new InheritableThreadLocal<>();

    /** Private Constructor. */
    private RequestIDHolder() { }

    /**
     * Get the current request ID as String.
     *
     * @return RequestID as String
     */
    public static String getRequestID() {
        return holder.get();
    }

    /**
     * Set current request ID.
     *
     * @param requestID Request id to set
     */
    public static void setRequestID(String requestID) {
        holder.set(requestID);
    }

    /**
     * Checks whether a request ID is set in the current thread scope.
     *
     * @return {@literal true} if so, otherwise {@literal false}
     */
    public static boolean hasRequestID() {
        return holder.get() != null;
    }

    /**
     * Cleanup thread local.
     */
    public static void destroy() {
        holder.remove();
    }
}

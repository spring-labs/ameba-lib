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
package org.ameba.http.identity;

import java.util.function.Consumer;

/**
 * A IdentityContextHolder stores keeps the current users identity for the current thread context.
 *
 * @author Heiko Scherrer
 */
public class IdentityContextHolder {

    private static final InheritableThreadLocal<String> identityHolder = new InheritableThreadLocal<>();

    /** Private Constructor. */
    private IdentityContextHolder() {}

    /**
     * Get the current Identity.
     *
     * @return Identity as String
     */
    public static String getCurrentIdentity() {
        return identityHolder.get();
    }

    /**
     * Set current Identity.
     *
     * @param identity User's identity to set
     */
    public static void setCurrentIdentity(String identity) {
        identityHolder.set(identity);
    }

    /**
     * Set current Identity.
     *
     * @param consumer A User's identity consumer function
     */
    public static void setCurrentIdentity(Consumer<String> consumer) {
        if (identityHolder.get() != null && !identityHolder.get().isEmpty()) {
            consumer.accept(identityHolder.get());
        }
    }

    /** Cleanup thread local. */
    public static void destroy() {
        identityHolder.remove();
    }
}

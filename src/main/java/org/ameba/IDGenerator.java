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
package org.ameba;

import java.io.Serializable;

/**
 * A IDGenerator is able to generate unique id of type {@code <T>}.
 *
 * @author Heiko Scherrer
 * @param <T> any type
 */
public interface IDGenerator <T extends Serializable> {

    /**
     * Generate an ID.
     *
     * @return ID
     */
    T generate();
}

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
package org.ameba.system;

/**
 * Created by axbsi01 on 17.06.2015.
 */
public class SystemSupport {

    private SystemSupport(){
    }

    /**
     * True if the current operating system is windows
     */
    public static final boolean IS_OS_WINDOWS = System.getProperty("os.name").toLowerCase().startsWith("win");

    /**
     * Path to the java temp directory as string
     */
    public static final String JAVA_TMP_DIR = System.getProperty("java.io.tmpdir");

}

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
package org.ameba.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * A SpringPointcuts.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.0
 * @since 1.0
 */
class SpringPointcuts {

    /**
     * Checks for {@link org.springframework.web.bind.annotation.RestController RestController} annotations.
     */
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public final void isRestController() {
    }

    /**
     * Matches {@link org.springframework.stereotype.Service Service} annotations.
     */
    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void isService() {
    }

    /**
     * Matches {@link org.ameba.annotation.TxService TxService} annotations.
     */
    @Pointcut("@within(org.ameba.annotation.TxService)")
    public void isTransactionalService() {
    }

    /**
     * Matches {@link org.springframework.stereotype.Repository Repository} annotations.
     */
    @Pointcut("@within(org.springframework.stereotype.Repository)")
    public final void isRepository() {
    }
}

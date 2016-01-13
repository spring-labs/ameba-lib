/*
 * Stamplets.
 *
 * This software module including the design and software principals used is and remains
 * the property of Heiko Scherrer (the initial authors of the project) with the understanding
 * that it is not to be reproduced nor copied in whole or in part, nor licensed or otherwise
 * provided or communicated to any third party without their prior written consent.
 * It must not be used in any way detrimental to the interests of the author.
 * Acceptance of this module will be construed as an agreement to the above. 
 *
 * All rights of Heiko Scherrer remain reserved.
 * Stamplets is an registered trademarks of Heiko Scherrer. Other products and company
 * names mentioned herein may be trademarks or trade names of the respective owner.
 * Specifications are subject to change without notice.
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
     * Uses this pointcut definition to combine with your custom pointcut.
     */
    @Pointcut("@target(org.springframework.web.bind.annotation.RestController)")
    public final void isRestController() {
    }

    /**
     * Uses this pointcut definition to combine with your custom pointcut.
     */
    @Pointcut("@target(org.springframework.stereotype.Service)")
    public void isService() {
    }

    /**
     * Uses this pointcut definition to combine with your custom pointcut.
     */
    @Pointcut("@target(org.springframework.stereotype.Repository)")
    public final void isRepository() {
    }
}

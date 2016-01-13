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
package org.ameba.annotation;

import org.ameba.aop.IntegrationLayerAspect;
import org.ameba.aop.PresentationLayerAspect;
import org.ameba.aop.ServiceLayerAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * A AspectsConfiguration.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableAspectJAutoProxy
class AspectsConfiguration {

    @Bean(name = ServiceLayerAspect.COMPONENT_NAME)
    public ServiceLayerAspect serviceLayerAspect() {
        return new ServiceLayerAspect();
    }

    @Bean(name = IntegrationLayerAspect.COMPONENT_NAME)
    public IntegrationLayerAspect integrationLayerAspect() {
        return new IntegrationLayerAspect();
    }

    @Bean(name = PresentationLayerAspect.COMPONENT_NAME)
    public PresentationLayerAspect presentationLayerAspect() {
        return new PresentationLayerAspect();
    }
}

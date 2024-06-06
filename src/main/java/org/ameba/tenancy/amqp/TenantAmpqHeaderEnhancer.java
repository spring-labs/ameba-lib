package org.ameba.tenancy.amqp;

import org.ameba.amqp.MessageHeaderEnhancer;
import org.ameba.tenancy.TenantHolder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.ameba.tenancy.amqp.TenantAmqpConfiguration.TENANT_AMPQ_HEADER;

/**
 * A TenantAmpqHeaderEnhancer is used on sender side to put the current tenant into the AMQP header {@value TenantAmqpConfiguration#TENANT_AMPQ_HEADER}.
 *
 * @author matjaz
 */
public class TenantAmpqHeaderEnhancer implements MessageHeaderEnhancer {

    /**
     * {@inheritDoc}
     */
    @Override
    public void enhance(RabbitTemplate rabbitTemplate) {
        rabbitTemplate.addBeforePublishPostProcessors(
            m -> {
                var currentTenant = TenantHolder.getCurrentTenant();
                if (currentTenant != null) {
                    m.getMessageProperties().getHeaders().put(TENANT_AMPQ_HEADER, currentTenant);
                }
                return m;
            }
        );
    }
}

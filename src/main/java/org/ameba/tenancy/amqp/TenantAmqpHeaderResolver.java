package org.ameba.tenancy.amqp;

import org.ameba.amqp.MessagePostProcessorProvider;
import org.ameba.tenancy.TenantHolder;
import org.springframework.amqp.core.MessagePostProcessor;

import static org.ameba.tenancy.amqp.TenantAmqpConfiguration.TENANT_AMPQ_HEADER;

/**
 * A TenantAmqpHeaderResolver is used on receiving side and retrieves the tenant from the AMQP header {@value TenantAmqpConfiguration#TENANT_AMPQ_HEADER}.
 *
 * @author matjaz
 */
public class TenantAmqpHeaderResolver implements MessagePostProcessorProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public MessagePostProcessor getMessagePostProcessor() {
        return (m -> {
            var tenant = (String) m.getMessageProperties().getHeaders().get(TENANT_AMPQ_HEADER);
            if (tenant != null) {
                TenantHolder.setCurrentTenant(tenant);
            }
            return m;
        });
    }
}

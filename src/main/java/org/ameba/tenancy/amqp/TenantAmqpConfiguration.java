package org.ameba.tenancy.amqp;

import org.ameba.amqp.MessageHeaderEnhancer;
import org.ameba.amqp.MessagePostProcessorProvider;
import org.ameba.annotation.ExcludeFromScan;
import org.ameba.tenancy.TenantHolder;
import org.ameba.tenancy.TenantMdc;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A TenantAmqpConfiguration is a Spring configuration class that is activated with AMQP support and bootstraps beans to propagate context
 * information over AMQP.
 *
 * @author matjaz
 */
@ExcludeFromScan
@ConditionalOnClass(org.springframework.amqp.rabbit.core.RabbitTemplate.class)
@Configuration
public class TenantAmqpConfiguration {

    public static final String TENANT_AMPQ_HEADER = "owms_tenant";

    @Bean
    public MessagePostProcessorProvider tenantAmqpHeaderResolver() {
        return new TenantAmqpHeaderResolver();
    }

    @Bean
    public MessageHeaderEnhancer tenantAmpqHeaderEnhancer() {
        return new TenantAmpqHeaderEnhancer();
    }

    @Bean
    @ConditionalOnBean(SimpleRabbitListenerContainerFactory.class)
    MethodInterceptor interceptor(SimpleRabbitListenerContainerFactory factory) {
        MethodInterceptor interceptor = invocation -> {
            try {
                TenantMdc.setContext(TenantHolder.getCurrentTenant());
                return invocation.proceed();
            } finally {
                TenantMdc.clearContext();
            }
        };
        factory.setAdviceChain(interceptor);
        return interceptor;
    }
}

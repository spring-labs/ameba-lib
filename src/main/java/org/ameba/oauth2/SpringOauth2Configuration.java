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
package org.ameba.oauth2;

import org.ameba.annotation.ExcludeFromScan;
import org.ameba.http.FilterStrategy;
import org.ameba.oauth2.issuer.IssuerRepository;
import org.ameba.oauth2.issuer.PersistentIssuerWhiteList;
import org.ameba.oauth2.parser.RSA256TokenParser;
import org.ameba.oauth2.tenant.TenantRepository;
import org.ameba.oauth2.tenant.TenantValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * A SpringOauth2Configuration can be imported but is excluded from component scan and
 * defines all Spring Beans that are required for JWT validation.
 *
 * <pre>
 *     &#064;Import(SpringOauth2Configuration.class)
 *     &#064;EntityScan(basePackages = {"org.ameba.oauth2.issuer", "org.ameba.oauth2.tenant"})
 *     &#064;EnableJpaRepositories(basePackages = {"org.ameba.oauth2.issuer", "org.ameba.oauth2.tenant"})
 * </pre>
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
@ExcludeFromScan
@Configuration
public class SpringOauth2Configuration {

    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private IssuerRepository issuerRepository;

    @Bean
    PersistentIssuerWhiteList persistentIssuerWhiteList(IssuerRepository issuerRepository) {
        return new PersistentIssuerWhiteList(issuerRepository);
    }

    @Bean
    RSA256TokenParser rsa256TokenParser() {
        return new RSA256TokenParser();
    }

    @Bean
    BearerTokenExtractor bearerTokenExtractor(IssuerWhiteList whiteList, List<TokenParser> parsers) {
        return new BearerTokenExtractor(whiteList, parsers);
    }

    @Bean
    JwtValidator jwtValidator() {
        return new TenantValidator(tenantRepository);
    }

    @Bean
    FilterStrategy filterStrategy(List<TokenExtractor> extractors, JwtValidator jwtValidator) {
        return new JwtValidationStrategy(extractors, jwtValidator);
    }
}

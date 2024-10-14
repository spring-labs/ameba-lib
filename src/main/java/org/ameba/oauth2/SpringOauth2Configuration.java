/*
 * Copyright 2015-2024 the original author or authors.
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

import com.auth0.jwk.JwkProvider;
import org.ameba.annotation.ExcludeFromScan;
import org.ameba.oauth2.parser.RSA256TokenParser;
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
 *     &#064;EntityScan(basePackageClasses = {IssuerPackage.class, TenantPackage.class})
 *     &#064;EnableJpaRepositories(basePackageClasses = {IssuerPackage.class, TenantPackage.class})
 * </pre>
 *
 * Required bean definitions: JwkProvider
 *
 * @author Heiko Scherrer
 */
@ExcludeFromScan
@Configuration
public class SpringOauth2Configuration {

    @Bean
    RSA256TokenParser rsa256TokenParser(JwkProvider jwkProvider) {
        return new RSA256TokenParser(jwkProvider);
    }

    @Bean
    BearerTokenExtractor bearerTokenExtractor(IssuerWhiteList whiteList, List<TokenParser> parsers) {
        return new BearerTokenExtractor(whiteList, parsers);
    }

    @Bean
    FilterStrategy filterStrategy(List<TokenExtractor> extractors, @Autowired(required = false) JwtValidator jwtValidator) {
        return new JwtValidationStrategy(extractors, jwtValidator);
    }
}

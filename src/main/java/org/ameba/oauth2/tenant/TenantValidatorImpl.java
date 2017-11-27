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
package org.ameba.oauth2.tenant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import org.ameba.oauth2.InvalidTokenException;
import org.ameba.oauth2.TokenValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.ameba.Constants.HEADER_VALUE_X_TENANT;

/**
 * A TokenValidatorImpl.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
@Component
class TenantValidatorImpl implements TokenValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantValidatorImpl.class);
    private final TenantRepository repository;

    private TenantValidatorImpl(TenantRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validate(Jwt jwt, HttpServletRequest request) {
        if (jwt instanceof Jws) {
            Jws<Claims> jws = (Jws) jwt;
            String issuer = jws.getBody().getIssuer();
            String realm = issuer.substring(issuer.lastIndexOf("/")+1, issuer.length());
            Optional<TenantEO> tenantEO = repository.findByHash(request.getHeader(HEADER_VALUE_X_TENANT));
            if (!tenantEO.isPresent()){
                throw new InvalidTokenException("Tenant to registered");
            }
            if (!tenantEO.get().sameRealm(realm)) {
                throw new InvalidTokenException("The issue does not match the configured REALM for the Tenant");
            }
            if (!tenantEO.get().getName().equals(jws.getBody().getAudience())) {
                throw new InvalidTokenException("The token has been issued for some other audience, is the token leaked or replayed?");
            }
            LOGGER.debug("{} has been translated into [{}]", HEADER_VALUE_X_TENANT, tenantEO.get().getName());
            request.setAttribute(HEADER_VALUE_X_TENANT, tenantEO.get().getName());
        } else {
            throw new InvalidTokenException("Only signed JWT are supported");
        }
    }
}

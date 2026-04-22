/*
 * Copyright 2005-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ameba.aop;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.ameba.annotation.EnableAspects;
import org.ameba.annotation.NotLogged;
import org.ameba.test.LogCaptureAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * A IntegrationLayerNotLoggedTest verifies {@link NotLogged} suppression on the integration layer.
 *
 * @author Heiko Scherrer
 */
@EnableAspects(propagateRootCause = true)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = IntegrationLayerNotLoggedTest.TestConfig.class)
class IntegrationLayerNotLoggedTest {

    static class TestConfig {
        @Bean
        MyIntegrationComponent component() {
            return new MyIntegrationComponent();
        }
    }

    @Autowired
    MyIntegrationComponent component;

    @BeforeEach
    public void clearLoggingStatements() {
        LogCaptureAppender.clearEvents();
    }

    @Test void testNotLoggedIsSuppressed() {
        try {
            component.notLogged();
            fail("Expected exception");
        } catch (Exception ex) {
            // expected
        }
        for (ILoggingEvent event : LogCaptureAppender.getEvents()) {
            if (event.getMessage() != null && event.getMessage().contains("Should not appear")) {
                fail("@NotLogged exception was logged by the integration aspect");
            }
        }
    }

    @Test void testPlainIsLogged() {
        try {
            component.logged();
            fail("Expected exception");
        } catch (Exception ex) {
            // expected
        }
        for (ILoggingEvent event : LogCaptureAppender.getEvents()) {
            if (event.getMessage() != null && event.getMessage().contains("Must be logged")) {
                return;
            }
        }
        fail("Plain exception was not logged by the integration aspect");
    }

    @Test void testNotLoggedInCauseChainIsSuppressed() {
        try {
            component.wrapsNotLogged();
            fail("Expected exception");
        } catch (Exception ex) {
            // expected
        }
        for (ILoggingEvent event : LogCaptureAppender.getEvents()) {
            if (event.getMessage() != null && event.getMessage().contains("wrapper of not-logged")) {
                fail("Exception with @NotLogged in its cause chain was logged by the integration aspect");
            }
        }
    }
}

@Repository
class MyIntegrationComponent {
    void notLogged() {
        throw new MyIntegrationNotLoggedException("Should not appear");
    }
    void logged() {
        throw new MyIntegrationException("Must be logged");
    }
    void wrapsNotLogged() {
        throw new MyIntegrationException("wrapper of not-logged", new MyIntegrationNotLoggedException("inner"));
    }
}

@NotLogged
class MyIntegrationNotLoggedException extends RuntimeException {
    MyIntegrationNotLoggedException(String message) {
        super(message);
    }
}

class MyIntegrationException extends RuntimeException {
    MyIntegrationException(String message) {
        super(message);
    }
    MyIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}

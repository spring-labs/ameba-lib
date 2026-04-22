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
import org.ameba.exception.BusinessRuntimeException;
import org.ameba.test.LogCaptureAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * A PresentationLayerNotLoggedTest verifies {@link NotLogged} suppression on the presentation layer.
 *
 * @author Heiko Scherrer
 */
@EnableAspects(propagateRootCause = true)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PresentationLayerNotLoggedTest.TestConfig.class)
class PresentationLayerNotLoggedTest {

    static class TestConfig {
        @Bean
        MyController controller() {
            return new MyController();
        }
    }

    @Autowired
    MyController controller;

    @BeforeEach
    public void clearLoggingStatements() {
        LogCaptureAppender.clearEvents();
    }

    @Test void testNotLoggedIsSuppressed() {
        try {
            controller.notLogged();
            fail("Expected exception");
        } catch (Exception ex) {
            // expected
        }
        for (ILoggingEvent event : LogCaptureAppender.getEvents()) {
            if (event.getMessage() != null && event.getMessage().contains("Should not appear")) {
                fail("@NotLogged exception was logged by the presentation aspect");
            }
        }
    }

    @Test void testPlainIsLogged() {
        try {
            controller.logged();
            fail("Expected exception");
        } catch (Exception ex) {
            // expected
        }
        for (ILoggingEvent event : LogCaptureAppender.getEvents()) {
            if (event.getMessage() != null && event.getMessage().contains("Must be logged")) {
                return;
            }
        }
        fail("Plain exception was not logged by the presentation aspect");
    }

    @Test void testNotLoggedInCauseChainIsSuppressed() {
        try {
            controller.wrapsNotLogged();
            fail("Expected exception");
        } catch (Exception ex) {
            // expected
        }
        for (ILoggingEvent event : LogCaptureAppender.getEvents()) {
            if (event.getMessage() != null && event.getMessage().contains("wrapper of not-logged")) {
                fail("Exception with @NotLogged in its cause chain was logged by the presentation aspect");
            }
        }
    }

    @Test void testNotLoggedBusinessRuntimeExceptionIsSuppressed() {
        try {
            controller.notLoggedBusiness();
            fail("Expected exception");
        } catch (Exception ex) {
            // expected
        }
        for (ILoggingEvent event : LogCaptureAppender.getEvents()) {
            if (event.getMessage() != null && event.getMessage().contains("Should not appear as BRE")) {
                fail("@NotLogged BusinessRuntimeException was logged by the presentation aspect");
            }
        }
    }
}

@RestController
class MyController {
    public void notLogged() {
        throw new MyPresentationNotLoggedException("Should not appear");
    }
    public void logged() {
        throw new MyPresentationException("Must be logged");
    }
    public void wrapsNotLogged() {
        throw new MyPresentationException("wrapper of not-logged", new MyPresentationNotLoggedException("inner"));
    }
    public void notLoggedBusiness() {
        throw new MyNotLoggedBusinessException("Should not appear as BRE");
    }
}

@NotLogged
class MyPresentationNotLoggedException extends RuntimeException {
    MyPresentationNotLoggedException(String message) {
        super(message);
    }
}

class MyPresentationException extends RuntimeException {
    MyPresentationException(String message) {
        super(message);
    }
    MyPresentationException(String message, Throwable cause) {
        super(message, cause);
    }
}

@NotLogged
class MyNotLoggedBusinessException extends BusinessRuntimeException {
    MyNotLoggedBusinessException(String message) {
        super(message);
    }
}

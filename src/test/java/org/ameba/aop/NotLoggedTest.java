/*
 * Copyright 2005-2023 the original author or authors.
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
import org.ameba.annotation.TxService;
import org.ameba.exception.ServiceLayerException;
import org.ameba.test.LogCaptureAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * A NotLoggedTest.
 *
 * @author Heiko Scherrer
 */
@EnableAspects(propagateRootCause = true)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = NotLoggedTest.TestConfig.class)
class NotLoggedTest {

    static class TestConfig {
        @Bean
        MyService service() {
            return new MyService();
        }
    }

    @Autowired
    MyService service;

    @BeforeEach
    public void clearLoggingStatements() {
        LogCaptureAppender.clearEvents();
    }

    @Test void testNotLogged() {
        try {
            service.notLogged();
        } catch (ServiceLayerException e) {
            boolean notFound = true;
            for (ILoggingEvent next : LogCaptureAppender.getEvents()) {
                if (next.getMessage().contains("Should not appear")) {
                    notFound = false;
                }
            }
            if (!notFound) {
                fail("Unexpected logged");
            }
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test void testPlain() {
        try {
            service.logged();
        } catch (ServiceLayerException e) {
            boolean found = false;
            for (ILoggingEvent next : LogCaptureAppender.getEvents()) {
                if (next.getMessage().contains("Must be logged")) {
                    found = true;
                }
            }
            if (!found) {
                fail("No logs captured");
            }
        } catch (Exception e) {
            fail("Unexpected exception");
            e.printStackTrace();
        }
    }
}

@TxService
class MyService {
    void notLogged() {
        throw new MyNotLoggedException("Should not appear");
    }
    void logged() {
        throw new MyException("Must be logged");
    }
}

@NotLogged
class MyNotLoggedException extends RuntimeException {
    public MyNotLoggedException(String message) {
        super(message);
    }
}

class MyException extends RuntimeException {
    public MyException(String message) {
        super(message);
    }
}

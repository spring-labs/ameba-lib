/*
 * Copyright 2015-2026 the original author or authors.
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
package org.ameba.http;

import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * A ResponseTest verifies the {@link Response} HTTP envelope: builder semantics, equality,
 * the {@code getFirst}/{@code getAny} accessors and the {@link Response#parse(String)} contract.
 *
 * @author Heiko Scherrer
 */
class ResponseTest {

    @Test void builderPopulatesAllFields() {
        var testee = Response.<String>newBuilder()
                .withMessage("ok")
                .withMessageKey("M.OK")
                .withHttpStatus("200")
                .withObj("a", "b")
                .build();

        assertThat(testee.getMessage()).isEqualTo("ok");
        assertThat(testee.getMessageKey()).isEqualTo("M.OK");
        assertThat(testee.getHttpStatus()).isEqualTo("200");
        assertThat(testee.getObj()).containsExactly("a", "b");
    }

    @Test void newBuilderReturnsFreshInstances() {
        assertThat(Response.newBuilder()).isNotSameAs(Response.newBuilder());
    }

    @Test void getFirstReturnsNullWhenObjMissing() {
        var empty = Response.<String>newBuilder().build();
        var withEmptyArray = Response.<String>newBuilder().withObj(new String[0]).build();

        assertThat(empty.getObj()).isNull();
        assertThat(withEmptyArray.getObj()).isEmpty();
    }

    @Test void getAnyRejectsNullKey() {
        var testee = Response.<String>newBuilder().build();

        assertThatThrownBy(() -> testee.getAny(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("key");
    }

    @Test void equalsAndHashCodeRespectAllFields() {
        var a = Response.<String>newBuilder().withMessage("m").withMessageKey("k").withHttpStatus("200").withObj("x").build();
        var b = Response.<String>newBuilder().withMessage("m").withMessageKey("k").withHttpStatus("200").withObj("x").build();
        var different = Response.<String>newBuilder().withMessage("m").withMessageKey("k").withHttpStatus("500").withObj("x").build();

        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
        assertThat(a).isNotEqualTo(different);
    }

    @Test void toStringContainsKeyFields() {
        var testee = Response.<String>newBuilder().withMessage("m").withMessageKey("k").withHttpStatus("200").withObj("x").build();

        assertThat(testee.toString()).contains("m", "k", "200", "x");
    }

    @Test void parseReturnsResponseForValidInput() throws ParseException {
        var json = "{\"message\":\"ok\",\"messageKey\":\"M.OK\",\"httpStatus\":\"200\",\"class\":\"String\",\"obj\":[\"x\"]}";

        var parsed = Response.parse(json, String.class);

        assertThat(parsed.getMessage()).isEqualTo("ok");
        assertThat(parsed.getMessageKey()).isEqualTo("M.OK");
        assertThat(parsed.getHttpStatus()).isEqualTo("200");
        assertThat(parsed.getObj()).containsExactly("x");
    }

    @Test void parseThrowsOnMissingMandatoryFields() {
        assertThatThrownBy(() -> Response.parse("{}", String.class))
                .isInstanceOf(ParseException.class)
                .hasMessageContaining("mandatory fields");
    }
}

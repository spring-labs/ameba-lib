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
package org.ameba.oauth2;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * A BearerTokenExtractorTest pins the {@code canExtract} contract for Bearer authorization headers.
 * Tests for the deeper {@code extract} path (which delegates to {@link DefaultTokenExtractor}) are
 * intentionally kept separate so that crypto/whitelist concerns are not mixed with header-shape checks.
 *
 * @author Heiko Scherrer
 */
class BearerTokenExtractorTest {

    private final BearerTokenExtractor testee = new BearerTokenExtractor(null, List.of());

    @Test void canExtractAcceptsWellFormedBearerJwt() {
        var result = testee.canExtract("Bearer aaa.bbb.ccc");

        assertThat(result.isExtractionPossible()).isTrue();
        assertThat(result.getReason()).isNull();
    }

    @Test void canExtractRejectsHeaderWithoutBearerPrefix() {
        var result = testee.canExtract("NotBearer aaa.bbb.ccc");

        assertThat(result.isExtractionPossible()).isFalse();
        assertThat(result.getReason()).isEqualTo("Not a valid JWT");
    }

    @Test void canExtractIsCaseSensitiveOnBearer() {
        assertThat(testee.canExtract("bearer aaa.bbb.ccc").isExtractionPossible()).isFalse();
        assertThat(testee.canExtract("BEARER aaa.bbb.ccc").isExtractionPossible()).isFalse();
    }

    @Test void canExtractRejectsTokensWithWrongNumberOfParts() {
        assertThat(testee.canExtract("Bearer aaa").isExtractionPossible()).isFalse();
        assertThat(testee.canExtract("Bearer aaa.bbb").isExtractionPossible()).isFalse();
        assertThat(testee.canExtract("Bearer aaa.bbb.ccc.ddd").isExtractionPossible()).isFalse();
    }

    @Test void canExtractRejectsBearerWithEmptyToken() {
        // "Bearer " alone strips to "" which splits to a single empty element.
        assertThat(testee.canExtract("Bearer ").isExtractionPossible()).isFalse();
    }

    @Test void canExtractReturnsCleanFailureForNullHeader() {
        var result = testee.canExtract(null);

        assertThat(result.isExtractionPossible()).isFalse();
        assertThat(result.getReason()).isEqualTo("No Authorization header");
    }

    @Test void extractThrowsInvalidTokenForNullHeader() {
        assertThatThrownBy(() -> testee.extract(null))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Not a valid Bearer token");
    }

    @Test void canExtractRejectsDoubleBearerHeader() {
        var result = testee.canExtract("Bearer Bearer aaa.bbb.ccc");

        assertThat(result.isExtractionPossible()).isFalse();
        assertThat(result.getReason()).isEqualTo("Not a valid JWT");
    }
}

/*
 * Copyright 2015-2020 the original author or authors.
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

import io.jsonwebtoken.Jwt;

/**
 * A ExtractionResult is an encapsulation of a token extraction process.
 *
 * @author Heiko Scherrer
 */
public final class ExtractionResult {

    private boolean extractionPossible;
    private String reason;
    private Jwt<?,?> jwt;

    /**
     * Extraction is possible.
     */
    public ExtractionResult() {
        this.extractionPossible = true;
    }

    /**
     * Extraction was possible with the given result {@code jwt}
     *
     * @param jwt The JWT
     */
    public ExtractionResult(Jwt<?,?> jwt) {
        this();
        this.jwt = jwt;
    }

    /**
     * Extraction is not possible cause of {@code reason}.
     *
     * @param reason The reason why extraction is not possible
     */
    public ExtractionResult(String reason) {
        this.reason = reason;
        this.extractionPossible = false;
    }

    public boolean isExtractionPossible() {
        return extractionPossible;
    }

    public String getReason() {
        return reason;
    }

    public Jwt<?,?> getJwt() {
        return jwt;
    }

    public boolean hasJwt() {
        return jwt != null;
    }
}

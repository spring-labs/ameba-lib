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
package org.ameba.http;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jayway.jsonpath.Configuration;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.jayway.jsonpath.JsonPath.read;

/**
 * An instance of Response is a transfer object that is used to encapsulate a server response to the client application.
 * It contains an array of items specific to the request. Refer to the concept of <a href="https://github.com/kevinswiber/siren">SIREN</a>.
 *
 * @author Heiko Scherrer
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Response<T extends Serializable> extends RepresentationModel<Response<T>> implements Serializable {

    public static final String CLASS = "class";
    /** A text message to transfer as server response. */
    private String message = "";

    /** A unique key to identify a message. */
    private String messageKey = "";

    /** An array ob objects that can be passed to the client to identify failure items. */
    private T[] obj;

    /** A http status code for this item. */
    private String httpStatus;

    private final Map<String, String> other = new HashMap<>();

    protected Response() {
    }

    private Response(String message, String messageKey, String httpStatus, T[] obj) {
        this.message = message;
        this.messageKey = messageKey;
        this.httpStatus = httpStatus;
        this.obj = obj;
    }

    private Response(Builder<T> builder) {
        message = builder.message;
        messageKey = builder.messageKey;
        obj = builder.obj;
        httpStatus = builder.httpStatus;
    }

    /**
     * Checks whether all mandatory fields are set on the String passed as {@literal s} and parses this String into a
     * valid instance.
     *
     * @param s The String to get the mandatory fields from
     * @return The instance
     * @throws ParseException In case the given String didn't match
     */
    public static <T extends Serializable> Response<T> parse(String s) throws ParseException {
        if (s.contains("message") &&
                s.contains("messageKey") &&
                s.contains("httpStatus") &&
                s.contains(CLASS)) {
            var d = Configuration.defaultConfiguration().jsonProvider().parse(s);
            String[] obj = read(d, "$.obj");
            var r = new Response<>(read(d, "$.message"), read(d, "$.messageKey"), read(d, "$.httpStatus"), obj);
            r.any();
        }
        throw new ParseException(String.format("String does not contain mandatory fields. [%s]", s), -1);
    }

    public static <T extends Serializable> Builder<T> newBuilder() {
        return new Builder<>();
    }

    @JsonAnyGetter
    protected Map<String, String> any() {
        if (getFirst() != null) {
            other.put(CLASS, getFirst().getClass().getSimpleName());
        }
        return other;
    }

    @JsonAnySetter
    protected void set(String name, String value) {
        if (getFirst() != null) {
            other.put(CLASS, getFirst().getClass().getSimpleName());
        }
    }

    /**
     * If exists, return the first element of response object array.
     *
     * @return First element or {@code null}
     */
    protected T getFirst() {
        if (obj == null || obj.length == 0) {
            return null;
        }
        return obj[0];
    }

    public String getMessage() {
        return message;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public T[] getObj() {
        return obj;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public String getAny(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key argument is null");
        }
        return other.get(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        var response = (Response) o;

        if (httpStatus != null ? !httpStatus.equals(response.httpStatus) : response.httpStatus != null) return false;
        if (message != null ? !message.equals(response.message) : response.message != null) return false;
        if (messageKey != null ? !messageKey.equals(response.messageKey) : response.messageKey != null) return false;
        return Arrays.equals(obj, response.obj);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (messageKey != null ? messageKey.hashCode() : 0);
        result = 31 * result + (obj != null ? Arrays.hashCode(obj) : 0);
        result = 31 * result + (httpStatus != null ? httpStatus.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Response{" +
                "httpStatus='" + httpStatus + '\'' +
                ", message='" + message + '\'' +
                ", messageKey='" + messageKey + '\'' +
                ", obj=" + Arrays.toString(obj) +
                '}';
    }

    /**
     * {@code Response} builder static inner class.
     */
    public static final class Builder<T extends Serializable> {
        private String message;
        private String messageKey;
        private T[] obj;
        private String httpStatus;

        private Builder() {
        }

        /**
         * Sets the {@code message} and returns a reference to this Builder so that the methods can be chained
         * together.
         *
         * @param val the {@code message} to set
         * @return a reference to this Builder
         */
        public Builder<T> withMessage(String val) {
            message = val;
            return this;
        }

        /**
         * Sets the {@code messageKey} and returns a reference to this Builder so that the methods can be chained
         * together.
         *
         * @param val the {@code messageKey} to set
         * @return a reference to this Builder
         */
        public Builder<T> withMessageKey(String val) {
            messageKey = val;
            return this;
        }

        /**
         * Sets the {@code obj} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code obj} to set
         * @return a reference to this Builder
         */
        @SafeVarargs
        public final Builder<T> withObj(T... val) {
            obj = val;
            return this;
        }

        /**
         * Sets the {@code httpStatus} and returns a reference to this Builder so that the methods can be chained
         * together.
         *
         * @param val the {@code httpStatus} to set
         * @return a reference to this Builder
         */
        public Builder<T> withHttpStatus(String val) {
            httpStatus = val;
            return this;
        }

        /**
         * Returns a {@code Response} built from the parameters previously set.
         *
         * @return a {@code Response} built with parameters of this {@code Response.Builder}
         */
        public Response<T> build() {
            return new Response<>(this);
        }
    }
}

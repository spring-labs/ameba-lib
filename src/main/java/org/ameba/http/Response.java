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
package org.ameba.http;

import static com.jayway.jsonpath.JsonPath.read;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jayway.jsonpath.Configuration;
import org.springframework.hateoas.ResourceSupport;

/**
 * An instance of Response is a transfer object that is used to encapsulate a server response to the client application. It contains an
 * array of items specific to the request. Compare to the concept of SIREN.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.1
 * @since 0.1
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Response<T extends Serializable> extends ResourceSupport implements Serializable {

    /**
     * Shortcut if the server responds with only one item.
     */
    /** A text message to transfer as server response. */
    private String message = "";
    /**
     * A unique key to identify a particular message. Note that this key can relate to the wrapped <tt>message</tt>, but it might not.
     */
    private String messageKey = "";
    /**
     * An array ob objects that can be passed to the client to identify failure items.
     */
    //@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
    private T obj[];
    /**
     * A http status code for this item.
     */
    private String httpStatus;

    private Map<String, String> other = new HashMap<>();

    /**
     * Constructor used for deserialization.
     */
    protected Response() {
    }

    /**
     * Create a new Response.
     *
     * @param message The message as text
     * @param messageKey A unique message key used to identify the message on client side
     * @param httpStatus An HTTP status
     */
    public Response(String message, String messageKey, String httpStatus) {
        this.message = message;
        this.messageKey = messageKey;
        this.httpStatus = httpStatus;
    }

    /**
     * Create a new Response.
     *
     * @param message The message as text
     * @param messageKey A unique message key used to identify the message on client side
     * @param httpStatus An HTTP status
     * @param obj An array of result entities
     */
    public Response(String message, String messageKey, String httpStatus, T[] obj) {
        this.message = message;
        this.messageKey = messageKey;
        this.obj = obj;
        this.httpStatus = httpStatus;
    }

    /**
     * Create a new Response.
     *
     * @param message The message as text
     * @param httpStatus An HTTP status
     */
    public Response(String message, String httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    /**
     * Checks whether all mandatory fields are set on the String passed as {@literal s} and parses this String into a valid instance.
     *
     * @param s The String to get the mandatory fields from
     * @return The instance
     * @throws ParseException In case the passen String didn't match
     */
    public static Response<?> parse(String s) throws ParseException {
        if (s.contains("message") &&
                s.contains("messageKey") &&
                s.contains("httpStatus") &&
                s.contains("class")) {
            Object d = Configuration.defaultConfiguration().jsonProvider().parse(s);
            String[] obj = read(d, "$.obj");
            Response<?> r = new Response<>(read(d, "$.message"), read(d, "$.messageKey"), read(d, "$.httpStatus"), obj );
            r.any();
        }
        throw new ParseException(String.format("String does not contain mandatory fields. [%s]", s), -1);
    }

    @JsonAnyGetter
    protected Map<String, String> any() {
        if (getFirst() != null) {
            other.put("class", getFirst().getClass().getSimpleName());
        }
        return other;
    }

    @JsonAnySetter
    protected void set(String name, String value) {
        if (getFirst() != null) {
            other.put("class", getFirst().getClass().getSimpleName());
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

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public T[] getObj() {
        return obj;
    }

    public void setObj(T[] obj) {
        this.obj = obj;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Map<String, String> getOther() {
        return other;
    }

    public void setOther(Map<String, String> other) {
        this.other = other;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Response response = (Response) o;

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
}

/*
 * openwms.org, the Open Warehouse Management System.
 * Copyright (C) 2014 Heiko Scherrer
 *
 * This file is part of openwms.org.
 *
 * openwms.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * openwms.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.ameba.exception;

import org.ameba.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

/**
 * A GatewayException.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class GatewayException extends BusinessRuntimeException {

    /**
     * Constructs with a message key and a message
     *
     * @param messageKey Message key
     * @param data    Additional implicit data passed to the caller
     */
    public GatewayException(String messageKey, Serializable[] data) {
        super(messageKey, data);
    }

    /**
     * Constructs with a message text a message key and a message
     *
     * @param message Message text
     * @param messageKey Message key
     * @param data    Additional implicit data passed to the caller
     */
    public GatewayException(String message, String messageKey, Serializable[] data) {
        super(message, messageKey, data);
    }

    /**
     * Create and return an instance with the given parameters and the message key {@link Messages#GW_GENERIC}.
     *
     * @param message Message text
     * @param data Additional implicit data passed to the caller
     * @return The instance
     */
    public static GatewayException createFrom(String message, Serializable... data) {
        return new GatewayException(message, Messages.GW_GENERIC, data);
    }
}

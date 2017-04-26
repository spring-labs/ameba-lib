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
package org.ameba.i18n;

import org.springframework.context.i18n.LocaleContextHolder;

/**
 * In contrast to the {@code AbstractTranslator} an AbstractSpringTranslator is more
 * flexible in the resolution of Locales. Instead of relying on the platform default
 * Locale, this class uses Spring's {@link LocaleContextHolder} to resolve the current
 * Locale. This can be handy in a web environment.
 *
 * Requested by: https://github.com/abraxas-labs/ameba-lib/issues/124
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
public abstract class AbstractSpringTranslator extends AbstractTranslator {

    /**
     * {@inheritDoc}
     *
     * Use Spring's LocaleContextHolder to retrieve the current Locale.
     * @see LocaleContextHolder
     */
    @Override
    public String translate(String key, Object... objects) {
        return getMessageSource().getMessage(key, objects, LocaleContextHolder.getLocale());
    }
}

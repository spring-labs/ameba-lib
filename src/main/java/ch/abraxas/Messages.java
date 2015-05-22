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
package ch.abraxas;

/**
 * A Messages contains all possible message keys.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Messages {

    /*~ Server status */
    public static final String SERVER_OK = "server.ok";
    public static final String SERVER_NOK = "server.nok";

    /*~ Security */
    public static final String BAD_CREDENTIALS = "server.bad.credentials";
    public static final String AUTHENTICATED = "server.authenticated";
    public static final String LOGOUT_FAILED = "server.logout.failed";

    /*~ Dossiers */
    public static final String DOSSIER_FOUND = "dossier.found";
    public static final String DOSSIERS_FOUND = "dossier.found.all";
    public static final String NO_DOSSIERS_FOUND = "dossier.not.found";
    public static final String UNKNOWN_DOSSIER = "dossier.unknown";
    public static final String DOSSIER_ALREADY_EXIST = "dossier.already.exist";
    public static final String MULTIPLE_DOSSIERS = "dossier.multiple";
    public static final String DOSSIER_NOT_EXIST = "dossier.not.exist";

    /*~ Documents */
    public static final String UNKNOWN_DOCUMENT = "unknown.document";
    public static final String MULTIPLE_DOCUMENTS = "multiple.documents";

    /*~ Folders */

    /*~ Generic */
    public static final String UNKNOWN_COMPONENT = "unknown.component";
    public static final String CREATED = "generic.created";
    public static final String NOT_FOUND = "not.found";
    public static final String ALREADY_EXISTS = "already.exists";
}

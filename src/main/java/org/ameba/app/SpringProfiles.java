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
package org.ameba.app;

/**
 * SpringProfiles is a definition of profiles used to configure the Spring ApplicationContexts.
 *
 * @author Heiko Scherrer
 * @version 1.1
 * @since 0.x
 */
public final class SpringProfiles {

	/** Activate this profile in development environment. */
	public static final String DEVELOPMENT_PROFILE = "DEV";
	/** Activate this profile in integration test environment (e.g. CI system). */
	public static final String IT_PROFILE = "IT";
	/** Activate this profile in User Acceptance Test environment. */
	public static final String UAT_PROFILE = "UAT";
	/** Activate this profile in production environment. */
	public static final String PRODUCTION_PROFILE = "PROD";
}

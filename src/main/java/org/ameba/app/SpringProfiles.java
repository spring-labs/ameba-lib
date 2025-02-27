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
package org.ameba.app;

/**
 * SpringProfiles is a definition of profiles used to configure the Spring ApplicationContexts.
 *
 * @author Heiko Scherrer
 */
public final class SpringProfiles {

	/*~ ------ Stages ------- */
	/** Activate this profile in development environment. */
	public static final String DEVELOPMENT_PROFILE = "DEV";
	/** Activate this profile in integration test environment (e.g. CI system). */
	public static final String IT_PROFILE = "IT";
	/** Activate this profile in User Acceptance Test environment. */
	public static final String UAT_PROFILE = "UAT";
	/** Activate this profile in production environment. */
	public static final String PRODUCTION_PROFILE = "PROD";

	/*~ ------ Operation Modes ------- */
	/** Used to define that the service is running in a distributed environment. */
	public static final String DISTRIBUTED = "DISTRIBUTED";

	/*~ ------ Asynchronous Messaging ------- */
	/**
	 * Activate this profile to enable asynchronous messaging capabilities.
	 *
	 * @deprecated Pick one of the concrete asynchronous messaging solutions instead, like {@link #AMQP}, {@link #MQTT} or {@link #KAFKA}.
	 */
	@Deprecated(forRemoval = true)
	public static final String ASYNCHRONOUS_PROFILE ="ASYNCHRONOUS";
	/** Activate this profile to enable asynchronous messaging capabilities over an AMQP message broker. */
	public static final String AMQP ="AMQP";
	/** Activate this profile to enable asynchronous messaging capabilities over a MQTT message broker. */
	public static final String MQTT ="MQTT";
	/** Activate this profile to enable asynchronous messaging capabilities over a Kafka message broker. */
	public static final String KAFKA ="KAFKA";

	private SpringProfiles() { }
}

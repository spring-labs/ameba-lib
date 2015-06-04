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
 * A LoggingCategories is a collection of cross-cutting logger categories that are used within a logging configuration
 * to write log messages not corresponding to Java classes or Java packages, but specific to application layer (as an
 * example).
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 0.1
 * @since 0.3
 */
public final class LoggingCategories {

	/** Category to log all errors thrown in the presentation layer. */
	public static final String PRESENTATION_LAYER_EXCEPTION = "PRESENTATION_LAYER_EXCEPTION";
	/** Category to log all errors thrown in the business layer. */
	public static final String SERVICE_LAYER_EXCEPTION = "SERVICE_LAYER_EXCEPTION";
	/** Category to log all errors thrown in the integration layer. */
	public static final String INTEGRATION_LAYER_EXCEPTION = "INTEGRATION_LAYER_EXCEPTION";
	/** Category to log each method call with the time spent. */
	public static final String LOG_MEASURED = "MEASURED";
	/** Category to log time consumption of operations performed by the integration layer on external systems. */
	public static final String INTEGARTION_LAYER_PROCESSING = "INTEGARTION_LAYER_PROCESSING";
}

# ameba-lib
[![Build status](https://github.com/spring-labs/ameba-lib/actions/workflows/master-build.yml/badge.svg)](https://github.com/spring-labs/ameba-lib/actions/workflows/master-build.yml)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Maven central](https://img.shields.io/maven-central/v/io.interface21/ameba-lib)](https://search.maven.org/search?q=a:ameba-lib)
[![Join the chat at https://gitter.im/openwms/org.openwms](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/openwms/org.openwms?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Ameba Lib is a collection of utils, exceptions, constants and other helpers used across projects and solutions. All dependencies are defined
in Maven `provided` scope to cut transitive dependencies.

## Usage
**Notice:** 
- Two development branches exist for the minimal supported Java version (jdk/17/dev branch) and the current supported Java version (master branch)
- Spring Boot 4.0.x compatible releases use ameba-lib version 4.3
- Spring Boot 3.5.x compatible releases use ameba-lib version 4.2
- Spring Boot 3.2.x compatible releases use ameba-lib version 4.1
- Spring Boot 2.7.x compatible releases use ameba-lib version 3.x
- Spring Boot 1.5.x compatible releases use ameba-lib version 1.x

| Ameba version | Spring Boot version | Java version | Supported until |
|---------------|---------------------|--------------|-----------------|
| 4.3.0         | 4.0.5               | 21           | TBD             |
| 4.2.0         | 3.5.4               | 21           | 4.3.0           |
| 4.2.0-jdk17   | 3.5.4               | 17           | 4.3.0-jdk17     |
| 4.1.1         | 3.2.5               | 21           | 4.2.0           |
| 4.1.1-jdk17   | 3.2.5               | 17           | 4.2.0-jdk17     |
| 4.0           | 3.2.5               | 21           | 4.1             |
| 3.1           | 2.7.18.RELEASE      | 11           | EOL             |
| 2.6           | 2.2.1.RELEASE       | 8            | EOL             |
| 1.11.1        | 1.5.3.RELEASE       | 8            | EOL             |

Add as Maven dependency
```
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>io.interface21</groupId>
                <artifactId>ameba-lib</artifactId>
                <version>4.3.1-SNAPSHOT</version>
            </dependency>
            <dependency>
                <groupId>io.interface21</groupId>
                <artifactId>ameba-lib</artifactId>
                <version>4.3.1-SNAPSHOT</version>
                <type>test-jar</type>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>io.interface21</groupId>
            <artifactId>ameba-lib</artifactId>
        </dependency>
        <dependency>
            <groupId>io.interface21</groupId>
            <artifactId>ameba-lib</artifactId>
            <type>test-jar</type>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <repositories>
        <repository>
            <id>sonatype-nexus-snapshots</id>
            <name>Snapshot Repository</name>
            <url>https://oss.sonatype.org/content/repositories/snapshots</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
            <releases>
                <enabled>false</enabled>
            </releases>
        </repository>
    </repositories>
```

To benefit from some abstract test classes use the `test-jar` for all other stuff go with 
the `jar`.

## What's in the box

Ameba-lib is a tool belt for Spring Boot services. Adding the jar pulls in **auto-configured infrastructure**
(response envelope, REST template wiring, AMQP wiring, call-context propagation, JPA auditing). Optional
capabilities (AOP, multi-tenancy, identity awareness) are activated with an `@Enable*` annotation on one of
your `@Configuration` classes.

| Category                   | Package                              | Activation                                          |
|----------------------------|--------------------------------------|-----------------------------------------------------|
| Base/validation/REST/AMQP  | `org.ameba.app`, `org.ameba.http`, `org.ameba.amqp` | Auto-configured                      |
| Call context propagation   | `org.ameba.http.ctx`                 | Auto-configured                                     |
| Request-ID filter          | `org.ameba.http.RequestIDFilter`     | Register as `FilterRegistrationBean`                |
| JPA / MongoDB base entities| `org.ameba.integration.{jpa,mongodb}`| Extend `BaseEntity` / `ApplicationEntity`           |
| Exceptions & i18n          | `org.ameba.exception`, `org.ameba.i18n` | Import types directly                            |
| Response envelope          | `org.ameba.http.Response`            | Import types directly                               |
| AOP aspects                | `org.ameba.aop`, `org.ameba.annotation` | `@EnableAspects`                                 |
| Multi-tenancy              | `org.ameba.tenancy`, `org.ameba.http.multitenancy`, `org.ameba.integration.hibernate` | `@EnableMultiTenancy` |
| Identity awareness         | `org.ameba.http.identity`            | `@EnableIdentityAwareness`                          |
| OAuth2 / JWT               | `org.ameba.oauth2`                   | Wire `JwtValidationStrategy` into your own filter   |
| Logback extensions         | `logback-appenders.xml`, `logback-loggers.xml`, `org.ameba.logging` | `<include>` from `logback.xml` |
| Bean mapping               | `org.ameba.mapping`                  | Inject `BeanMapper` (Dozer by default)              |

## Quick start

Adding the jar alone wires the following into your Spring Boot application without further configuration
(listed in `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`):

- `BaseConfiguration`, `ValidationConfiguration` – base bootstrap and a `Validator` bean.
- `RestTemplateConfiguration`, `LoadBalancedRestTemplateConfiguration` – a ready-to-use `RestTemplateBuilder`
  (load-balanced variant conditional on Spring Cloud LoadBalancer).
- `WebMvcConfiguration` – registers HTTP client interceptors that propagate call context, identity, and tenant
  headers with any `RestTemplate` that picks up the `baseRestTemplateInterceptors` bean list.
- `AmqpConfiguration` – overrides the default `SimpleRabbitListenerContainerFactory` to apply
  `MessageHeaderEnhancer` and `MessagePostProcessorProvider` beans (conditional on the RabbitMQ AMQP classpath).
- `CallContextWebMvcConfiguration`, `CallContextAmqpConfiguration`, `CallContextFeignConfiguration`,
  `OpenTelemetryCallContextConfiguration`, `DefaultCallContextProviderConfiguration` – call-context propagation
  over HTTP, AMQP, Feign, and OTel.
- `IdentityFeignConfiguration`, `TenantFeignConfiguration`, `TenantAmqpConfiguration` – Feign and AMQP adapters
  for identity and tenant headers (activated once `@EnableIdentityAwareness` / `@EnableMultiTenancy` is present).
- `BaseJpaConfiguration` – JPA auditor wiring so `@CreatedBy` / `@LastModifiedBy` on `BaseEntity` works.

A minimal Spring Boot application therefore only needs:

```java
@SpringBootApplication
@FilteredComponentScan(basePackages = "com.acme")     // optional, see "Component scanning"
@EnableAspects                                        // optional, turns on AOP aspects
@EnableMultiTenancy(separationStrategy = SeparationStrategy.SCHEMA)   // optional
@EnableIdentityAwareness                              // optional
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

## Feature reference

### JPA & MongoDB base entities

`org.ameba.integration.jpa.BaseEntity` contributes a `Long` primary key (`C_PK`), an optimistic-locking column
(`C_OL`), and the four auditing columns (`C_CREATED`, `C_CREATED_BY`, `C_UPDATED`, `C_UPDATED_BY`). Auditing is
wired through `BaseJpaConfiguration`; the `AuditorAware` resolves the current identity from
`IdentityContextHolder` when `@EnableIdentityAwareness` is active.

```java
@Entity
@Table(name = "T_CUSTOMER")
public class Customer extends ApplicationEntity {
    @Column(name = "C_NAME") private String name;
}
```

- `BaseEntity` – technical PK, optimistic locking, auditing timestamps and users.
- `ApplicationEntity extends BaseEntity` – adds `pKey` (UUID-seeded `C_PID`) as a stable business key that
  survives database migrations; `equals`/`hashCode` are based on `pKey`.
- `TypedEntity<ID>` – marker interface both entities implement; used by `FindOperations` / `SaveOperations`.

For MongoDB, `org.ameba.integration.mongodb.BaseEntity` provides the same shape (`_id`, `_ol`, `_created`,
`_updated`). Put `@EnableMongoAuditing` on one of your `@Configuration` classes to activate the timestamps.

### Repository operation abstractions

The marker interfaces `FindOperations<T, ID>` and `SaveOperations<T, ID>` in `org.ameba.integration` declare
`findAll` / `findById(id)` / `save` / `saveAll` contracts independent of the persistence provider. Use them
when a service layer API should not leak Spring Data types.

### Response envelope

`org.ameba.http.Response<T>` wraps a payload with `message`, `messageKey`, `httpStatus`, and a dynamic
`class` attribute (SIREN-inspired) so that non-HATEOAS-aware clients (e.g. native mobile apps) can discriminate
response types.

```java
return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(Response.<CustomerVO>newBuilder()
                .withMessage("Created")
                .withMessageKey("customer.created")
                .withHttpStatus(HttpStatus.CREATED.toString())
                .withObj(customerVO)
                .build());
```

Example payload:

```json
{
  "message": "Created",
  "messageKey": "customer.created",
  "httpStatus": "201",
  "obj": [ { "pKey": "a204c4ce-...", "name": "ACME" } ],
  "class": "CustomerVO"
}
```

`org.ameba.http.AbstractBase<T>` is a HATEOAS `RepresentationModel` superclass for view objects that adds
`ol` / `createDt` / `lastModifiedDt` fields, serialised in Zulu time (`yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'`).

### Exception hierarchy

All ameba exceptions carry a `messageKey` (for i18n via `Translator`) plus an optional `Serializable[] data`
payload that travels back to the caller.

![Exception hierarchy][4]

- `TechnicalRuntimeException` – infrastructure / programming errors. Subclass: `IntegrationLayerException`,
  `PresentationLayerException`, `ServiceLayerException`.
- `BusinessRuntimeException` – expected business failures; can take a `Translator` to resolve the message
  directly from the key.
- `BehaviorAwareException` extends `BusinessRuntimeException` with an HTTP status mapping and a
  `toResponse()` that produces a ready-to-return `ResponseEntity<Response>`. Subclasses:
  `NotFoundException` (404), `ResourceExistsException` (409), `ResourceChangedException` (409),
  `BadRequestException` (400), `GatewayException` (502), `GatewayTimeoutException` (504).

Combine with `org.ameba.annotation.NotLogged` on exception types whose stack traces should never hit the log.

### Internationalised messages

`org.ameba.i18n.Translator` and the abstract bases `AbstractTranslator` / `AbstractSpringTranslator` turn a
`messageKey` + args into a localised string using Spring's `MessageSource`. The auxiliary
`NestedReloadableResourceBundleMessageSource` (in `org.ameba.i18n`) lets you chain multiple resource bundles
without giving up hot-reload.

```java
throw new NotFoundException(translator, "customer.notfound", new Serializable[]{ id }, id);
```

### AOP aspects

Add `@EnableAspects` to any `@Configuration` class and put `spring-aspects` on the runtime classpath. Four
aspects kick in against pointcuts defined in `org.ameba.aop.Pointcuts` (override by placing your own
`org.ameba.aop.Pointcuts` class earlier on the classpath):

| Aspect                   | Method-tracing category    | Exception translation | Exception category             |
|--------------------------|----------------------------|-----------------------|--------------------------------|
| `PresentationLayerAspect`| –                          | –                     | `PRESENTATION_LAYER_EXCEPTION` |
| `ServiceLayerAspect`     | `SERVICE_LAYER_ACCESS`     | yes                   | `SERVICE_LAYER_EXCEPTION`      |
| `IntegrationLayerAspect` | `INTEGRATION_LAYER_ACCESS` | yes                   | `INTEGRATION_LAYER_EXCEPTION`  |
| `MeasuredAspect`         | `MEASURED`                 | –                     | –                              |

Set the respective SLF4J category to `INFO` for tracing and `ERROR` for exception logging (the bundled
`logback-loggers.xml` already does this). Register an `ExceptionTranslator` bean to translate custom
exception types into framework-recognised ones.

```java
@TxService                                // transactional + validated service stereotype
public class CustomerService {

    @Measured                             // log method execution time under MEASURED
    public CustomerVO create(@Valid CustomerVO vo) { ... }

    @NotTransformed                       // opt a single method out of exception translation
    public void raw() { ... }
}
```

Supporting annotations in `org.ameba.annotation`:

- `@TxService` – meta-annotation combining `@Service`, `@Transactional`, and `@Validated`.
- `@Measured` – mark classes or public methods for timing by `MeasuredAspect`.
- `@NotLogged` – suppress stack-trace logging for an exception type.
- `@NotTransformed` – opt a single service/integration method out of exception translation.
- `@Public` – document that a type is intentionally `public`.
- `@Default` – mark a preferred constructor/method (e.g. for MapStruct).
- `@ExcludeFromScan` – exclude a `@Configuration` from component scans.
- `@FilteredComponentScan` – `@ComponentScan` variant with the exclude filter above pre-applied.

For measured REST controllers, use the composed stereotype:

```java
@MeasuredRestController("customers")   // = @RestController + @Measured + @Timed
@RequestMapping("/v1/customers")
public class CustomerController { ... }
```

### Multi-tenancy

Activate with `@EnableMultiTenancy` on any `@Configuration` class:

```java
@Configuration
@EnableMultiTenancy(
    separationStrategy = SeparationStrategy.SCHEMA,   // or NONE (default), COLUMN
    throwIfNotPresent  = true,                         // reject requests without tenant header
    urlPatterns        = { "/api/*" },
    defaultDatabaseSchema = "public",
    tenantSchemaPrefix    = "t_"
)
public class AppConfig {}
```

What it wires up:

1. `MultiTenantSessionFilter` – reads the `X-Tenant` (or `Tenant`) HTTP header and stores the value in
   `TenantHolder` (an `InheritableThreadLocal`). Also mirrors the tenant into SLF4J MDC for the
   `TenantDiscriminator` so Logback can route logs per tenant.
2. `TenantClientRequestInterceptor` (auto-configured) – propagates the header to downstream `RestTemplate`
   calls.
3. `TenantFeignConfiguration.TenantRequestInterceptor` – same for Feign clients.
4. `TenantAmqpConfiguration` – header propagation across RabbitMQ messages via
   `TenantAmpqHeaderEnhancer` and `TenantAmqpHeaderResolver`.
5. `SeparationStrategy.SCHEMA` – imports `SchemaBasedTenancyConfiguration`, which installs a Hibernate
   `MultiTenantConnectionProvider` (`DefaultMultiTenantConnectionProvider`) that switches the JDBC schema per
   tenant (`<tenantSchemaPrefix><tenant>`). Schemas must exist already – Hibernate DDL is not executed across
   schemas. `SeparationStrategy.COLUMN` imports `ColumnBasedTenancyConfiguration` for discriminator-column
   separation.

Access the current tenant anywhere:

```java
TenantHolder.currentTenant().ifPresent(t -> logger.info("tenant={}", t));
```

Utility `org.ameba.tenancy.TenantSchemaUtils` applies a tenant to a JDBC `Connection`;
`IllegalTenantException` is thrown when required tenant data is missing.

### Identity awareness

Activate with `@EnableIdentityAwareness`:

```java
@Configuration
@EnableIdentityAwareness(
    throwIfNotPresent    = true,
    urlPatterns          = { "/api/*" },
    identityResolverStrategy = HeaderAttributeResolverStrategy.class   // default
)
public class SecurityConfig {}
```

This registers `IdentityFilter`, which uses a pluggable `IdentityResolverStrategy` to extract the identity
(default: `X-Identity` header via `HeaderAttributeResolverStrategy`; alternative:
`TokenResolverStrategy` for JWT-subject extraction) and stores it in `IdentityContextHolder`.
`IdentityClientRequestInterceptor` / `IdentityRequestInterceptor` / `IdentityAmqpConfiguration` propagate the
same identity downstream over HTTP, Feign, and AMQP.

```java
String whoami = IdentityContextHolder.currentIdentity().orElse("anonymous");
```

### Call context propagation

A `CallContext` object (caller, trace ID, free-form details) travels with every request and is exposed through
`CallContextHolder`:

```java
CallContextHolder.setCaller(() -> "order-service");
CallContextHolder.getOptionalCallContext()
        .map(CallContext::getTraceId)
        .ifPresent(MDC::put);
```

Transport adapters (auto-configured):

- `CallContextWebMvcConfiguration` – servlet filter that reads the base64-encoded `X-CallContext` header and
  populates the holder; client-side `CallContextClientRequestInterceptor` writes it out.
- `CallContextFeignConfiguration.CallContextRequestInterceptor` – same for Feign.
- `CallContextAmqpConfiguration` – enhancer/resolver for AMQP.
- `OpenTelemetryCallContextConfiguration` – picks up the trace ID from an active OpenTelemetry `Span` (falls
  back to `SimpleCallContextProvider` when OTel is not on the classpath).

Extend `CallContextProvider` to plug in alternative trace sources.

### Request-ID filter

`org.ameba.http.RequestIDFilter` honours an existing `X-RequestID` header or mints a new ID via any
`IDGenerator<String>` (default: `JdkIDGenerator` using `UUID.randomUUID()`). Register it explicitly:

```java
@Bean
FilterRegistrationBean<RequestIDFilter> requestIdFilter() {
    var reg = new FilterRegistrationBean<>(new RequestIDFilter(new JdkIDGenerator()));
    reg.addUrlPatterns("/*");
    return reg;
}
```

Read from anywhere with `RequestIDHolder.getRequestID()`.

### AMQP integration

With RabbitMQ on the classpath, `AmqpConfiguration` installs a `SimpleRabbitListenerContainerFactory` that
runs collected `MessagePostProcessorProvider` beans (inbound post-processing) and configures the outbound
`RabbitTemplate` through `RabbitTemplateConfigurable` + `MessageHeaderEnhancer` beans. Typical usage:

```java
@Bean
MessageHeaderEnhancer myHeaders() {
    return msg -> msg.getMessageProperties().setHeader("X-Source", "customer-svc");
}

@Bean
MessagePostProcessorProvider tenantInbound() {
    return () -> List.of(message -> {
        Object tenant = message.getMessageProperties().getHeaders().get("X-Tenant");
        if (tenant != null) TenantHolder.setCurrentTenant(tenant.toString());
        return message;
    });
}
```

The Call-Context, Identity, and Tenant modules ship ready-made enhancer/resolver beans that use exactly this
hook, so cross-service AMQP traffic stays contextually consistent without additional code.

### OAuth2 / JWT support

`org.ameba.oauth2` provides composable building blocks for validating JWTs in inbound requests. It does **not**
register a filter by itself – wire `JwtValidationStrategy` into your own `ContainerRequestFilter` or
`OncePerRequestFilter`:

```java
@Bean
OncePerRequestFilter jwtFilter(List<TokenExtractor> extractors, JwtValidator validator) {
    var strategy = new JwtValidationStrategy(extractors, validator);
    return new OncePerRequestFilter() {
        @Override protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain c)
                throws ServletException, IOException {
            strategy.doFilter(req, res);
            c.doFilter(req, res);
        }
    };
}
```

Moving parts:

- `TokenExtractor` – `BearerTokenExtractor` pulls the raw JWT from the `Authorization` header;
  `DefaultTokenExtractor` validates the issuer against an `IssuerWhiteList` before delegating to a matching
  `TokenParser` (ships with `HS512TokenParser` and `RSA256TokenParser` under `parser/`).
- `IssuerWhiteList` – either `ConfigurationIssuerWhiteList` (YAML/properties) or
  `PersistentIssuerWhiteList` (JPA-backed, with `JwksUrlRepository` for rotating keys).
- `TenantValidator` – validates that the tenant carried on the request is configured for the token issuer.
- `JwtValidator` – implement extra business checks (audience, scopes, revocation).

### Logback extensions

Include the bundled fragments in your `logback.xml`:

```xml
<configuration>
    <property name="MODULE_NAME" value="customer-svc"/>
    <!-- <property name="LOG_PATH"   value="/var/log/customer-svc"/> -->

    <include resource="logback-appenders.xml"/>
    <include resource="logback-loggers.xml"/>

    <root level="INFO">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="LOGFILE"/>
    </root>
</configuration>
```

Appenders defined in `logback-appenders.xml`:

| Appender | Destination                                      | Purpose                                             |
|----------|--------------------------------------------------|-----------------------------------------------------|
| STDOUT   | stdout                                           | Useful for tests and containerised deployments      |
| LOGFILE  | `<LOG_PATH>/BOOT-<MODULE_NAME>.log`              | Application / trace logging                         |
| EXCFILE  | `<LOG_PATH>/BOOT-<MODULE_NAME>.exlog`            | Exception logging (`*_LAYER_EXCEPTION` categories)  |
| TSL      | `<LOG_PATH>/BOOT-<MODULE_NAME>.tslog`            | `SERVICE_LAYER_ACCESS` / `INTEGRATION_LAYER_ACCESS` |

Output paths fall back in this order: `$LOG_PATH` → `$CATALINA_BASE` → `$LOG_TEMP` → `java.io.tmpdir`. The
tenant segment defaults to `BOOT` when no tenant is bound. For Loki deployments include
`logback-loki.xml` instead.

Extensions in `org.ameba.logging`:

- `TenantDiscriminator` – sift appender discriminator keying on `TenantHolder.getCurrentTenant()`.
- `ThreadIdProvider` – a numeric counter exposed as a property for the Logback pattern, so concurrent test
  runs become readable.

Log categories used across the aspects and propagation machinery are constants on
`org.ameba.LoggingCategories` (`SERVICE_LAYER_ACCESS`, `INTEGRATION_LAYER_EXCEPTION`, `MEASURED`, `BOOT`,
`CALLCONTEXT`, …).

### Bean mapping

`org.ameba.mapping.BeanMapper` is a minimal, implementation-agnostic mapping facade.
`DozerMapperImpl` is provided out of the box, along with `LocalDateConverter`, `LocalDateTimeConverter`,
and `ZonedDateTimeConverter` for the Java 8 date-time types.

```java
@Autowired BeanMapper mapper;
CustomerVO vo  = mapper.map(customer, CustomerVO.class);
List<CustomerVO> vos = mapper.map(customers, CustomerVO.class);
mapper.mapFromTo(customer, existingVO);    // merges into an existing target
```

Swap the implementation by providing your own `BeanMapper` bean (MapStruct, ModelMapper, …). Dozer is an
optional dependency – supply it yourself.

### Utility constants and helpers

- `org.ameba.app.SpringProfiles` – canonical profile names (`DEV`, `IT`, `UAT`, `PROD`, `DISTRIBUTED`,
  `AMQP`, `MQTT`, `KAFKA`).
- `org.ameba.Constants` – canonical HTTP header names (`HEADER_VALUE_X_TENANT`, `HEADER_VALUE_X_IDENTITY`,
  `HEADER_VALUE_X_REQUESTID`, `HEADER_VALUE_X_CALLERID`, `HEADER_VALUE_X_CALL_CONTEXT`) and API date
  patterns.
- `org.ameba.IDGenerator<T>` / `JdkIDGenerator` – inject your own strategy for correlation IDs.
- `org.ameba.system.ValidationUtil.validate(validator, bean, groups...)` – throws
  `ConstraintViolationException` with a readable message listing the invalid property paths.
- `org.ameba.Identifiable<T>` – marker interface for entities exposing an identifier.

## Development process
 Contribution welcome. The development process is kept lean, without the need to apply any IDE formatter templates. Just a few rules to
 follow:

  - All Java files must have the Apache License header on top
  - All Java types must provide a meaningful Javadoc comment with the initial author (`@author`). The first sentence in Javadoc is used as
 headline and needs to be a short but meaningful description of the type class.

 ````
 /**
  * A SecurityConfigurers class is a collector of interfaces that provides a configuration option for security related topics.
  *
  * @author Heiko Scherrer
  */
 ````

  - Public API methods have to be documented

### How to release
 A release is built from the `master` branch. At first all required feature branches need to be merged into the `master` branch. Only if
 the `master` branch builds successfully the release can be done. We follow the simple [Feature branch principle](https://de.atlassian.com/git/tutorials/comparing-workflows#feature-branch-workflow) 

 Checkout the master branch and build it locally with Javadocs and sources. Then use the release plugin to setup versions, git tags and
 upload to artifact repository.

 ````
 $ mvn clean package -Drelease
 $ mvn release:prepare
 $ mvn release:perform
 ````

 Follow these naming conventions:

  - A release version should follow the MM(P)(E) pattern: `<Mayor>.<Minor>(.<Patch>)(-<Extension>)`

  Mayor, Minor and Patch are increasing numbers and follow the rules of [Apache ARP release strategy][1]. The patch version number is only
  applied if the release is a bug fix release of a feature version. An Extension may be applied for release candidates or milestone
  releases, e.g. `-RC1` or `-M1`


 [1]: https://apr.apache.org/versioning.html#strategy
 [2]: https://github.com/spring-labs/ameba-lib/issues/69
 [3]: https://github.com/spring-labs/ameba-lib/issues/79
 [4]: src/site/resources/exceptions.png
 [5]: https://github.com/spring-labs/ameba-lib/issues/166

[logstashconf]: src/main/resources/logstash.conf

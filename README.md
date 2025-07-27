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
- Spring Boot 3.5.x compatible releases use ameba-lib version 4.2
- Spring Boot 3.2.x compatible releases use ameba-lib version 4.1
- Spring Boot 2.7.x compatible releases use ameba-lib version 3.x
- Spring Boot 1.5.x compatible releases use ameba-lib version 1.x

| Ameba version | Spring Boot version | Java version | Supported until |
|---------------|---------------------|--------------|-----------------|
| 4.1.2         | 3.2.5               | 21           | 4.2             |
| 4.1.1-jdk17   | 3.2.5               | 17           | 4.2             |
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
                <version>4.2.0-SNAPSHOT</version>
            </dependency>
            <dependency>
                <groupId>io.interface21</groupId>
                <artifactId>ameba-lib</artifactId>
                <version>4.2.0-SNAPSHOT</version>
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

## Core Features

- Spring Data extensions
- Useful AOP aspects
- Common exception classes
- Web & MVC extensions
- Mapper abstraction
- Multi-tenancy
- Logging extensions (Logback & Logstash)
- OAuth2 Token Parsing and Handling

### Spring Data extensions (1.4+)

Since version 1.4+ Ameba provides base classes for JPA entities ([#69][2], [#166][5]) as well as for Spring Data MongoDB entity classes
([#79][3]). These common used types provide unique key definitions, version fields and timestamp fields to store date created and modified.
An additional feature is the abstraction of Spring Data repositories. All this is put in package `org.ameba.integration`.

### Useful AOP aspects (1.2+)

Four aspects are implemented to track method calls around application layers. Three of them measure and trace the method execution time at
a defined logging category. The pointcut definition where the aspect actually takes place is pre-defined in `org.ameba.aop.Pointcuts`. This
definition can be overridden by putting a customized `org.ameba.aop.Pointcuts` class on the classpath. In addition to method tracing some
aspects although care about exception translation and offer an extension point to translate custom exceptions.

Ameba AOP support is enabled by including the package `org.ameba.annotation` in component-scan **or** by using the `@EnableAspects`
annotation on a custom `@Configuration` class. Furthermore `org.springframework:spring-aspects` needs to be at the classpath at runtime.

| Aspect (classname)           | Method Tracing Logging Category | Exception Translation  | Exception Logging Category   |
| ---------------------------- |:------------------------------- |:----------------------:|:---------------------------- |
| PresentationLayerAspect      | --                              | --                     | PRESENTATION_LAYER_EXCEPTION |
| ServiceLayerAspect           | SERVICE_LAYER_ACCESS            | X                      | SERVICE_LAYER_EXCEPTION      |
| IntegrationLayerAspect       | INTEGRATION_LAYER_ACCESS        | X                      | INTEGRATION_LAYER_EXCEPTION  |
| MeasuredAspect               | MEASURED                        | --                     | --                           |

For method tracing the SLF4J loglevel has to be configured to `INFO`, exception logging need to be configured to level `ERROR` instead.
Since 2.2 logging of exception stack traces can be turned off for specific exception types that are marked with `org.ameba.annotation.NotLogged`.

### Common exception classes (0.2+)

 Exception classes we have used over and over again in projects were re-implemented in ameba-lib. All of them encapsulate a message key that
 can be used to translate the actual message text. Some kind of exceptions are of technical nature, whereas other exceptions express a
 behavior.

![Exception hierarchy][4]

Referenced issues: [#1](https://github.com/spring-labs/ameba-lib/issues/1), [#2](https://github.com/spring-labs/ameba-lib/issues/2),
[#8](https://github.com/spring-labs/ameba-lib/issues/8), [#21](https://github.com/spring-labs/ameba-lib/issues/21),
[#37](https://github.com/spring-labs/ameba-lib/issues/37)

### Web & MVC extensions

Usually you expose resources in a RESTful way. But in practise we've seen that a client application, written in languages like Objective-C,
needs some help with the actual response kind of RESTful API. Of course, we have HATEOAS and HAL but on the other end of the wire, the
client need to know what is the expected type of response. That's why we put the resource into an _envelope_ - the
`org.ameba.http.Response`. Beside the actual response entities, this class tracks a http status for each wrapped response object, has a
message text and key and provides an arbitrary string dictionary that may be used to store the response type. On IOS there is no library to
deal with HATEOAS responses in a comfortable way. Many solutions use [Restkit](https://github.com/RestKit/RestKit) and need to parse the response in an old-fashioned
low-level way.

A server using ameba-lib may respond with:

```
{
  "message" : "Created",
  "messageKey" : "generic.created",
  "obj" : [ {
    "userId" : "a204c4ce-adc2-4d32-a2f6-64be710933ad",
    "name" : "admin",
    "version" : "f7afa81b-6b77-41e5-bca9-872d9842c38b",
    "lastModifiedDate" : [ 2014, 2, 16, 5, 28, 18, 866000000 ],
    "_identifier" : "56cfd462e4b008cd0d16d3b0"
  } ],
  "httpStatus" : "201",
  "class" : "User"
}

```

The client application may first read the status code of the http response, then the status code of each response item and then the
type of response entity (e.g. `"class" : "User"`. With this type information the client can then select the
appropriate converter to convert from JSON into the corresponding client specific class. A similar concept is included in [JSON Siren]
(https://github.com/kevinswiber/siren).

Beside the response encapsulation ameba-lib provides an abstract base class, that provides HATEOAS and Jackson support (`org.ameba.http.AbstractBase`).
Some filter implementations for multi-tenancy and SLF4J context propagation are provided as well.

### Mapper abstraction (0.7+)

In first place we use [Dozer](http://dozer.sourceforge.net) as mapping library. But this dependency is optional, and other mapper libraries can be used as well. A
common interface is defined were client applications can rely on. This is a Java 5 generic interface that makes it easy to map at compile
time. Why we need to map at all in Java is a different discussion. Since version 0.7 a `org.ameba.mapping.BeanMapper` interface is provided
to applications that need to map object structures. Since the same version the `org.ameba.mapping.DozerMapperImpl` and several Dozer
converters exist.

### Multi-tenancy (1.0+)

Multi-tenancy support requires at first determination of the current tenant and afterwards actions, mostly on persistent data, that need to
be taken depending on the tenant.

Determination of the tenant can be realized using a web filter `org.ameba.http.MultiTenantSessionFilter` (1.0+). This filter tries to get
a http header attribute called `X-Tenant` or `Tenant` and stores it in a threadlocal variable. On business- or integration layer this
context information is used to separate log files or to separate between databases, database schemas or database tables (up to the specific
solution). Look at the [tenancy sample](https://github.com/spring-labs/tenancy-sample) to understand how this works on database level. Separating
the log files is special. Ameba-lib uses SLF4J to abstract from the underlying logging framework. In case of context-aware data (like the
tenant name) needs to be populated down to the underlying logging library, SLF4J make it easy to work with [Logback](http://logback.qos.ch/).
SLF4J smoothly populates the logback context with its own context. If you're using other frameworks, like Log4j you need to implement a
custom _Context Populator_ that ready the SLF4J MDC/NDC and populates the log4j MDC/NDC properly.

Starting with 1.7 the configuration of multi-tenancy support can be done much more elegant by using the classlevel annotation
`@EnableMultiTenancy`. No manual filter registration needs to be done anymore.

#### Data record separation

New since 2.0: Tenant separation on database schema level. With the `@EnableMultiTenancy` annotation it is now possible to
define a data separation strategy for relational databases. Currently only the `SeparationStrategy.SCHEMA` is
supported and by default database separation is turned off (`SeparationStrategy.NONE`).

**Requirements and restrictions**
- Requires Hibernate 5.x and Spring Boot 2.x
- Only database schema separation supported yet (2.0)
- Database schema must already exist and cannot be created with Hibernate creation strategies on the fly

Referenced issues:
- [#102](https://github.com/spring-labs/ameba-lib/issues/102)
- [#141](https://github.com/spring-labs/ameba-lib/issues/141)

### Logging extensions (1.7+)

Starting with version 1.7 some useful logging extensions were added. At first a `ThreadIdProvider` is used to identify each thread in a
concurrent test run. By default, logback does only provide a meaningless thread name. But a thread counter can now be configured to display
the current value in the log message. To get the full power of Ameba log extensions just include the `logback-appenders.xml` and
`logback-loggers.xml` into your logback.xml:

 ````
 <configuration>
 
     <property name="MODULE_NAME" value="stamplets"/>
 
     <include resource="logback-appenders.xml" />
     <include resource="logback-loggers.xml" />
 
     <logger name="org.foo" level="ERROR"/>
 
     <root level="DEBUG">
         <appender-ref ref="STDOUT"/>
         <appender-ref ref="LOGFILE"/>
     </root>
 
 </configuration>
 ```` 

The following appender names are defined: 

| Appender name | Outputs to                              | Description                              |
| ------------- |:--------------------------------------- |:---------------------------------------- |
| STDOUT        | stdout                                  | Print sto stdout. Useful for test output |
| LOGFILE       | java.io.tmpdir/BOOT-<MODULE_NAME>.log   | Standard program or trace logging        |
| EXCFILE       | java.io.tmpdir/BOOT-<MODULE_NAME>.exlog | Exception logs                           |
| TSL           | java.io.tmpdir/BOOT-<MODULE_NAME>.tslog | Technical service logging. Logs method execution consumption |

The module name can be configured as logback property:

 ```` 
     <property name="MODULE_NAME" value="my-module"/>
 ```` 

`BOOT` is the default tenant name. The logging is basically multitenant-aware. If no active tenant is set, the default is used as prefix.
A tenant can be set by adding a property called `Tenant` to the `org.slf4j.MDC`. See `org.ameba.http.SLF4JMappedDiagnosticContextFilter`.

The logfile path can be configured by setting the logback property:

 ```` 
     <property name="LOG_PATH" value="/tmp"/>
 ```` 

By default, ameba logging first tries to find the configured $LOG_PATH property. If this property does not exist, it looks up $CATALINA_BASE
to check if the application is running inside Tomcat. If even this does not exist it tries to find a logback property named $LOG_TEMP, and
if that does not exist either, it will log to java.io.tmpdir.

Notice: The output pattern is defined to be aligned to the Grok pattern that is used in combination with Logstash ([logstash.conf][logstashconf]).

### OAuth2 Token Parsing and Handling (1.11+)

Java package `org.ameba.oauth2` contains all types to ease the handling of OAuth2 and OpenID Connect JWT parsing, validation and handling.
Notice that the structure nor the content of the tokens are not defined by the OAuth2 specification. Often the JWT format is used as token
format with various signing algorithms. An application may define a servlet filter or a `javax.ws.rs.container.ContainerRequestFilter` to
introduce OAuth2 Token handling. This filter could then delegate to an instance of `org.ameba.oauth2.JwtValidationStrategy` to integrate
Ameba OAuth2 support.

Basically Ameba OAuth2 Support works as the follows;
 
 - The filter strategy uses Extractors and Validators to extract tokens from the incoming request and to validate them
 - The `BearerTokenExtractor` extracts a Bearer token from the Authorization header
 - The `DefaultTokenExtractor` uses the unsigned part of the JWT and validates the token issuer against an `IssuerWhiteList` first,
 afterward it uses one of the `TokenParsers` to extract and parse the token under consideration of the token signature
 - The `TenantValidator` should be used when a Tenant identifier exists and the Tenant is configured to work with the Token issuer. 

Extension Points:
 
 - Support additional signing algorithms and implement another `TokenParser`
 - Implement your own Repository to retrieve whitelist information and implement `IssuerWhiteList`
 - Implement your own `JwtValidator`

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

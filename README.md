# ameba-lib

[![Build status][travis-image]][travis-url]
[![License][license-image]][license-url]
[![Quality][codacy-image]][codacy-url]
[![Gitter][gitter-image]][gitter-url]

Ameba Lib is a collection of utils, exceptions, constants and other helpers used across projects and solutions. All dependencies are defined
in Maven `provided` scope to cut transitive dependencies.

## Core Features

- [Spring Data extensions] [Spring Data extensions (1.4+)]
- Useful AOP aspects
- Common exception classes
- Web & MVC extensions
- Mapper abstraction
- Multi-tenancy

### Spring Data extensions (1.4+)

Since version 1.4+ Ameba provides base classes for JPA entities ([#69][2]) as well as for Spring Data MongoDB entity classes ([#79][3]).
These common used types provide unique key definitions, version fields and timestamp fields to store creation and modified date. An
additional feature is the abstraction of Spring Data repositories.

### Useful AOP aspects (1.2+)

Three aspects are implemented to track method calls around application layers. Each aspect measures and traces the method execution time at
a defined logging category. The pointcut definition where the aspect actually takes place is pre-defined in `org.ameba.aop.Pointcuts`. This
definition can be overridden by putting a customized `org.ameba.aop.Pointcuts` class onto the classpath. In addition to method tracing some
aspects although care about exception translation and offer an extension point to translate custom exceptions.

Ameba AOP support is enabled by including the package `org.ameba.annotation` in component-scan **or** by using the `@EnableAspects`
annotation on a custom `@Configuration` class. Furthermore `org.springframework:spring-aspects` needs to be at the classpath at runtime.

| Aspect (classname)           | Method Tracing Logging Category | Exception Translation  | Exception Logging Category   |
| ---------------------------- |:------------------------------- |:----------------------:|:---------------------------- |
| PresentationLayerAspect      | --                              | --                     | PRESENTATION_LAYER_EXCEPTION |
| ServiceLayerAspect           | SERVICE_LAYER_ACCESS            | X                      | SERVICE_LAYER_EXCEPTION      |
| IntegrationLayerAspect       | INTEGRATION_LAYER_ACCESS        | X                      | INTEGRATION_LAYER_EXCEPTION  |

For method tracing the SLF4J loglevel has to be configured to `INFO`, exception logging need to be configured to level `ERROR` instead.

### Common exception classes (0.2+)

 Exception classes we have used over and over again in projects were re-implemented in ameba-lib. All of them encapsulate a message key that
 can be used to translate the actual message text. Some kind of exceptions are of technical nature, whereas other exceptions express a
 behavior.

![Exception hierarchy][4]

Referenced issues: [#1](https://github.com/abraxas-labs/ameba-lib/issues/1), [#2](https://github.com/abraxas-labs/ameba-lib/issues/2),
[#8](https://github.com/abraxas-labs/ameba-lib/issues/8), [#21](https://github.com/abraxas-labs/ameba-lib/issues/21),
[#37](https://github.com/abraxas-labs/ameba-lib/issues/37)

### Web & MVC extensions

Usually you expose resources in a RESTful way. But in practise we've seen that a client application, written in languages like Objective-C, needs
some help with the actual response type of a RESTful API. Of course we have HATEOS and HAL but on the other end of the wire, the client
need to know what is the expected type of response. That's why we put the resource into an _envelope_ the `org.ameba.http.Response`. Beside
the actual response(s), this class tracks a http status for each wrapped response object, has a message text and key for all and provides an
arbitrary string dictionary that can be used to encode the response type. IOS clients do not have higher level libraries to deal with
HATEOS responses, often they use [Restkit](https://github.com/RestKit/RestKit) and need to parse the response in a old-fashioned way.

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
type of response entity (e.g. `"class" : "User"`. With the information of the response entity the client can then select the
appropriate converter to convert from JSON into the correct client specific class. A similar concept is described by XXX

Beside the response encapsulation ameba-lib provides an abstract base class, that provides HATEOS and Jackson support (`org.ameba.http.AbstractBase`).
Some filter implementations for multi-tenancy and SLF4J context propagation are provided as well.

### Mapper abstraction (0.7+)

In first place we use [Dozer](http://dozer.sourceforge.net) as mapping library. But this dependency is optional, and other mapper libraries can be used as well. A common
interface is defined were client applications can rely on. This is a Java 5 generic interface that makes it easy to map at compile time.
Why we need to map at all in Java is a different discussion. Since version 0.7 a `org.ameba.mapping.BeanMapper` interface is provided to
applications that need to map object structures. Since the same version the `org.ameba.mapping.DozerMapperImpl` and several Dozer converters
exist. We'd a look at several other Java mapping frameworks and have chosen the one with no compile time dependencies - just in a
declarative way - thers than Dozer use Java annotations for example.

### Multi-tenancy (1.0+)

Multi-tenancy support requires at first determination of the current tenant and afterwards actions, mostly on persistent data, that need to
be taken depending on the tenant.

Determination of the tenant can be realized using a web filter `org.ameba.http.MultiTenantSessionFilter` (1.0+). This filter tries to get
a http header attribute called `X-Tenant` or `Tenant` and stores it in a threadlocal variable. On business- or integration layer this context
information is used to separate log files or to separate between databases, database schemas or database tables (up to the specific solution).
Have a look at the [tenancy sample](https://github.com/spring-labs/tenancy-sample) to understand how this works on database level. Separating
the log files is special. Ameba-lib uses SLF4J to abstract from the underlying logging framework. In case context-aware data (like the
tenant name) needs to be populated down to the underlying logging library, SLF4J make it easy to work with [Logback](http://logback.qos.ch/).
SLF4J smoothly populates the logback context with its own context. If you're using other frameworks, like Log4j you need to implememt a
custom _Context Populator_ that ready the SLF4J MDS/NDC and popultes the log4j MDC/NDC properly.

## Development process

 Contribution welcome. The development process is kept lean, without the need to apply any IDE formatter templates. Just a few rules to
 follow:

  - All Java files must have the Apache License header on top
  - All Java types must provide a meaningful Javadoc comment with the initial author (`@author`), the version of ameba when the type was
    introduced (`@since`) and a manually managed type version (`@version`). The first sentence in Javadoc is used as headline and needs to
    be a short but meaningful description of the type class.

 ````
 /**
  * A SecurityConfigurers class is a collector of interfaces that provides a configuration option for security related topics.
  *
  * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
  * @version 1.1
  * @since 1.4
  */
 ````

  - Public API methods have to be documented

## How to release

 A release is built from the `master` branch. At first all required feature branches need to be merged into the `develop` branch. Only if
 the `develop` branch builds successfully this can be merged into master.

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
 [2]: https://github.com/abraxas-labs/ameba-lib/issues/69
 [3]: https://github.com/abraxas-labs/ameba-lib/issues/79
 [4]: src/site/resources/exceptions.png

[travis-image]: https://img.shields.io/travis/abraxas-labs/ameba-lib.svg?style=flat-square
[travis-url]: https://travis-ci.org/abraxas-labs/ameba-lib
[license-image]: http://img.shields.io/:license-Apache2.0-blue.svg?style=flat-square
[license-url]: LICENSE
[codacy-image]: https://www.codacy.com/project/badge/1acf14eee0824d97b9eb54d956c404fe
[codacy-url]: https://www.codacy.com/app/openwms/ameba-lib
[gitter-image]: https://badges.gitter.im/Join%20Chat.svg
[gitter-url]: https://gitter.im/abraxas-labs/ameba-lib?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge

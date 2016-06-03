# ameba-lib

[![Build status][travis-image]][travis-url]
[![License][license-image]][license-url]
[![Quality][codacy-image]][codacy-url]
[![Gitter][gitter-image]][gitter-url]

Ameba Lib is a collection of utils, exceptions, constants and other helpers used across projects and solutions. All dependencies are defined
in Maven `provided` scope to cut transitive dependencies.

## Core Features

- Spring Data extensions
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

### Common exception classes

 Exception classes we have used over and over again in projects were re-implemented in ameba-lib. All of them encapsulate a message key that
 can be used to translate the actual message text. Some kind of exceptions have a technical nature, whereas others express a meaningful
 business exception.

![Exception hierarchy][4]

### Web & MVC extensions

TBD.

### Mapper abstraction

TBD.

### Multi-tenancy

TBD.

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

# ameba-lib

[![Build status][travis-image]][travis-url]
[![License][license-image]][license-url]
[![Quality][codacy-image]][codacy-url]
[![Gitter][gitter-image]][gitter-url]

Ameba Lib is a collection of utils, exceptions, constants and other helpers used across projects and solutions. All dependencies are defined as Maven `provided` scope to cut transitive dependencies.

## Core Features

- Spring Data extensions
- Useful AOP aspects
- Common exception classes
- Web & MVC extensions
- Mapper abstraction
- Multi-tenancy


## Development process

 Contribution welcome. The development process is kept lean, without the need to apply any IDE formatter templates. Just a few rules to
 follow:

  - All Java files must have the Apache License header on top
  - All Java types must provide a meaningful Javadoc comment with the initial author (`@author`), version of ameba since when this type
    exists (`@since`) and a manually managed type version (`@version`)
  - Public API must documented

 ````
 /**
  * A SecurityConfigurers class is a collector of interfaces that provide a configuration option for security related topics.
  *
  * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
  * @version 1.1
  * @since 1.4
  */
 ````

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

  - A release version should follow the MM(P)(E) pattern: <Mayor>.<Minor>(.<Patch>)(-<Extension>)
  Mayor, Minor and Patch are increasing numbers and follow the rules of [Apache ARP release strategy][1]. The patch version number is only
  applied if the release is really a bug fix release of a feature version. An extensions may be applied for release candidates of milestone
  releases, i.e. `-RC1` or `-M1`


 [1]: https://apr.apache.org/versioning.html#strategy

[travis-image]: https://img.shields.io/travis/abraxas-labs/ameba-lib.svg?style=flat-square
[travis-url]: https://travis-ci.org/abraxas-labs/ameba-lib
[license-image]: http://img.shields.io/:license-Apache2.0-blue.svg?style=flat-square
[license-url]: LICENSE
[codacy-image]: https://www.codacy.com/project/badge/1acf14eee0824d97b9eb54d956c404fe
[codacy-url]: https://www.codacy.com/app/openwms/ameba-lib
[gitter-image]: https://badges.gitter.im/Join%20Chat.svg
[gitter-url]: https://gitter.im/abraxas-labs/ameba-lib?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge

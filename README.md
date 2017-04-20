# neo4j-drivers

[![Build Status](https://travis-ci.org/szarnyasg/neo4j-drivers.svg?branch=master)](https://travis-ci.org/szarnyasg/neo4j-drivers)

The `ReactiveDriver` class takes a Neo4j Driver and enhances it with _reactive_ (or _incremental_) change notifications by applying the [Decorator pattern](https://en.wikipedia.org/wiki/Decorator_pattern).

The `EmbeddedTestkitDriver` class starts an embedded Neo4j instance and provides the Driver interface to access its data. This greatly simplifies testing Neo4j-based applications. The driver can use two kinds of `GraphDatabase` instances:
1. [`ImpermanentGraphDatabase`](https://github.com/neo4j/neo4j/blob/3.2/community/kernel/src/test/java/org/neo4j/test/ImpermanentGraphDatabase.java) (default)

2. [`EmbeddedGraphDatabase`](https://github.com/neo4j/neo4j/blob/3.2/community/kernel/src/main/java/org/neo4j/kernel/internal/EmbeddedGraphDatabase.java) (if the client specifies the `storeDir` parameter)

## Using the libraries

To install to your local Maven repository, issue the following command:

```
./gradlew publishT
```

This will invoke the `publishToMavenLocal` goal.

To use this from a Gradle project, add the following dependency:

```
compile group: 'neo4j-drivers', name: '...', version: '0.0.3-SNAPSHOT'
```

## Deploying binary artifacts

Use the following command:

```
./gradlew upload
```

Copy the files from the repository to wherever you'd like to deploy your artifacts.

## License

The `neo4j-embedded-testkit-driver` project is available under the [GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html) license. The `neo4j-reactive-driver` and the `neo4j-driver-util` projects are available under the [Eclipse Public License v1.0](http://www.eclipse.org/legal/epl-v10.html).

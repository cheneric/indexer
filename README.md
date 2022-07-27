## `Project`

### Overview

This is a Spring Boot starter project template for Java. 

### Technologies

  * Spring Boot (application framework)
  * Webflux (REST)
  * Jackson (JSON)

### How to run

There are 2 ways to run the application:

1. Natively
2. In a Docker container

#### 1. Run natively

To run in a native environment, **ensure OpenJDK 17+ is installed**.

Then, in the project root directory:

1. Build the project:

```shell
indexer  $ ./gradlew build
```

2. Run the project:

```shell
indexer  $ java -jar build/libs/project-0.0.1-SNAPSHOT.jar <directory-path-to-index>
```

If no directory path to index is provided, then a default testing directory will be used.

[//]: # (3. In a browser, open http://localhost:8080/)

#### 2. Run in a Docker container

TODO

[//]: # (To run in a Docker container, **ensure you have Docker installed**.)

[//]: # ()
[//]: # (Then, in the project root directory:)

[//]: # ()
[//]: # (1. Build the Docker image:)

[//]: # ()
[//]: # (```shell)

[//]: # (indexer  $ docker build --rm -t chen.eric/indexer:latest .)

[//]: # (```)

[//]: # ()
[//]: # (2. Run the Docker image in interactive mode:)

[//]: # ()
[//]: # (```shell)

[//]: # (indexer  $ docker run -it --rm --name indexer chen.eric/indexer:latest)

[//]: # (```)

[//]: # (3. In a browser, open http://localhost:8080/)

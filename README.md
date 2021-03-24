# Diff Search service

REST Api which stores and process encoded data to detect differences between them.


# Introduction

In this README file you'll find enough information to Install, Run and test this application. 
Also you will find answers to tech questions asked for this test, and I will also provide information to how this was implemented, and how this project could improve its performance.


## Prerequisites

* Java JDK 1.8 : https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
* Maven 3 : https://maven.apache.org/install.html
* Docker: https://docs.docker.com/install/
* Docker-compose: https://docs.docker.com/compose/install/

## Installing

 There are two ways for running this application. Running `mvn clean spring-boot:run` or we can do the Docker way, which I will explain:

* First we need to Clone this repository:

    > git clone https://github.com/Gonmontero/diff-search-service.git

* Open Terminal and check Maven is configured correctly:

    > mvn -v

      Command should respond with:

      Apache Maven 3.6.0 (97c98ec64a1fdfee7767ce5ffb20918da4f719f3)
      Maven home: /Library/apache-maven-3.6.0
      Java version: 1.8.0_202, vendor: Oracle Corporation, runtime: /Library/Java/JavaVirtualMachines/jdk1.8.0_202.jdk/Contents/Home/jre
      Default locale: en_US, platform encoding: UTF-8
      OS name: "mac os x", version: "10.14.3", arch: "x86_64", family: "mac"

      (Note that java version is 1.8)

 * Change your current directory to the location of the project and run:

    > mvn clean package

        Expected result should look like this:

        [INFO] --- spring-boot-maven-plugin:2.1.3.RELEASE:repackage (repackage) @ diff-search-service ---
        [INFO] Replacing main artifact with repackaged archive
        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time:  25.320 s
        [INFO] Finished at: 2021-03-24T15:20:31-03:00
        [INFO] ------------------------------------------------------------------------

 * Once the process has finished, start the Docker daemon and run:

    > docker-compose build

 * Last but not least, we have to tell Docker to start the image we have just built:

    > docker-compose up

         That should start the application and run it in the localhost Port 8080

         The expected result we are looking for is:
         dss_1  |  INFO [main] org.apache.catalina.startup.Catalina.start Server startup in 13382 ms


## API Documentation

Once the Service is up and running, use Swagger UI for an easier interaction with the service:

- Local ENV: http://localhost:8080/diff-search-service/swagger-ui.html#/

Endpoint Name | Http Request | Description |
| --- | --- | --- |
Register Diff | POST http://localhost:8080/diff-search-service/v1/diff/{id}/{side} | This will register a new Diff given an "id", "side" and a encoded string of "data" in the Body Request. Refer to Swagger UI for more information. |
| Process Diff | POST http://localhost:8080/diff-search-service/v1/diff/{id} | Process a Diff that has been registered and returns it's result |


## Project Decisions

- The project was built under the REST architectural style using SpringFramework.
- Implemented a Docker container in order to scale its functionality if needed.
- JPA was the ORM of choice as the persistence layer.
- Implemented an in-memory database named H2 in order to speed delivery time.

## Suggestions

- The process that searches for differences in the stored data can potentially timeout if the data is large enough.
 A different take on this problem could be using a queue system, where data could be processed and consulted later on in a different endpoint.

- Idempotency: A cache layer could result beneficial to reduce processing time. If the stored Diff has already been processed,
 and it's data has not changed, then returning a stored result from the previous process would reduce CPU consumption and time consumption. 

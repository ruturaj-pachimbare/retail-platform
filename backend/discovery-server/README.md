# TechZone Discovery Server

Netflix Eureka registry for the TechZone Retail Platform.

## Technology

- Java 21
- Spring Boot 3.5.7
- Spring Cloud 2025.0.0
- Spring Cloud Netflix Eureka Server

## Run locally

From this directory:

```powershell
mvn clean spring-boot:run
```

Or build and run the executable JAR:

```powershell
mvn clean package
java -jar target/discovery-server-0.0.1-SNAPSHOT.jar
```

The Eureka Dashboard is available at <http://localhost:8761>.

Health endpoint: <http://localhost:8761/actuator/health>.

## Registry configuration

The server has `register-with-eureka` and `fetch-registry` disabled because it is the registry, not a client of another registry. Business services will be configured as Eureka clients in a later milestone.

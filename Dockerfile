FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /
COPY pom.xml .
RUN mvn dependency:go-offline
COPY /src /src
COPY .env .
RUN mvn -f /pom.xml clean package

FROM openjdk:17-jdk-slim
COPY --from=build /target/*.jar application.jar
COPY --from=build src/main/resources/db-init/init.sql ./src/main/resources/db-init/init.sql
COPY .env .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]
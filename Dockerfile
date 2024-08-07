FROM openjdk:17-jbk-slim
COPY target/*.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
# Dockerfile
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/Foyer-1.4.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
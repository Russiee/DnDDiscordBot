FROM openjdk:11-jdk-slim
ARG TOKEN_ARG
ENV TOKEN = $TOKEN_ARG
COPY target/discordbot-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
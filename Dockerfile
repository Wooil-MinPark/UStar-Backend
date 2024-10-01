# Build stage
FROM openjdk:17-jdk-slim as build
WORKDIR /workspace/app

COPY gradle gradle
COPY build.gradle settings.gradle gradlew ./
COPY src src

RUN ./gradlew build -x test

# Run stage
FROM openjdk:17-jdk-slim
VOLUME /tmp
ARG JAR_FILE=/workspace/app/build/libs/*.jar
COPY --from=build ${JAR_FILE} app.jar
COPY --from=build /workspace/app/src/main/resources/application.properties /app/application.properties
COPY --from=build /workspace/app/src/main/resources/application-dev.properties /app/application-dev.properties
ENTRYPOINT ["java", "-jar", "/app.jar"]
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

# application.properties와 application-dev.properties 복사
COPY src/main/resources/application-dev.properties /app/application.properties

# ENTRYPOINT 수정
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.config.location=file:/app/application.properties"]
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
COPY src/main/resources/application-docker.properties /app/application.properties

# application.properties 내용 출력을 위한 스크립트 추가
COPY <<EOF /app/start.sh
#!/bin/sh
#echo "Contents of application.properties:"
#cat /app/application.properties
echo "Starting application..."
java -jar /app.jar --spring.config.location=file:/app/application.properties
EOF

RUN chmod +x /app/start.sh

# ENTRYPOINT 수정
ENTRYPOINT ["/app/start.sh"]
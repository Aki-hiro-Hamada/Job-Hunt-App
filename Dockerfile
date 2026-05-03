# syntax=docker/dockerfile:1
# Multi-stage: Maven でビルドし、JRE のみで実行（Render.com 向け）

FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn
COPY src ./src

RUN chmod +x mvnw && ./mvnw -B -DskipTests package

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends dumb-init \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/job-hunting-app-*.jar /app/app.jar

# Render は環境変数 PORT を渡す。アプリ側は application.properties の server.port=${PORT:8081} で受ける
EXPOSE 8081

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0"

USER 65534:65534
ENTRYPOINT ["dumb-init", "java", "-jar", "/app/app.jar"]

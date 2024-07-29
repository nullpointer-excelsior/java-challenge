FROM openjdk:17.0.1-jdk-slim AS build

ENV GRADLE_VERSION 8.8

WORKDIR /build

RUN apt-get update && apt-get install -y wget unzip && rm -rf /var/lib/apt/lists/*
RUN wget -q "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" -O gradle.zip \
    && unzip -q gradle.zip \
    && rm gradle.zip \
    && mv "gradle-${GRADLE_VERSION}" /usr/lib/gradle \
    && ln -s /usr/lib/gradle/bin/gradle /usr/bin/gradle

COPY src src
COPY build.gradle.kts .
COPY settings.gradle.kts .
RUN gradle bootJar

FROM openjdk:17.0.1-jdk-slim

WORKDIR /app

COPY --from=build /build/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
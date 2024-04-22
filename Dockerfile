# syntax=docker/dockerfile:1

# Create a stage for resolving and downloading dependencies.
FROM eclipse-temurin:21-jdk-jammy as deps

WORKDIR /clean
WORKDIR /build

# Copy the gradlew wrapper with executable permissions.
COPY --chmod=0755 gradlew gradlew
COPY gradle/ gradle/
COPY loadEnv.gradle /build/
COPY azure.gradle /build/

# Create the build/libs directory
RUN mkdir -p build/libs

# Download dependencies as a separate step to take advantage of Docker's caching.
# Leverage a cache mount to /root/.gradle so that subsequent builds don't have to
# re-download packages.
RUN --mount=type=bind,source=build.gradle,target=build.gradle \
    --mount=type=cache,target=/root/.gradle ./gradlew build -x test

# Create a stage for building the application based on the stage with downloaded dependencies.
FROM deps as package

WORKDIR /build

COPY ./src src/
RUN --mount=type=bind,source=build.gradle,target=build.gradle \
    --mount=type=cache,target=/root/.gradle \
    ./gradlew build -x test && \
    mv build/libs/CurrencyExchangeService-$(./gradlew properties -q | grep "version:" | awk '{print $2}').jar build/libs/app.jar && \
    ls -la build/libs
# Create a new stage for running the application that contains the minimal
# runtime dependencies for the application.
FROM eclipse-temurin:21-jre-jammy AS final

# Create a non-privileged user that the app will run under.
ARG UID=10001
RUN groupadd docker && \
    adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser && \
    usermod -aG docker appuser
USER appuser

# Copy the executable from the "package" stage.
COPY --from=package build/libs/app.jar app.jar

ARG SERVER_PORT=$SERVER_PORT
EXPOSE ${SERVER_PORT}

ENTRYPOINT [ "java", "-jar", "-Dserver.port=${SERVER_PORT}", "app.jar" ]
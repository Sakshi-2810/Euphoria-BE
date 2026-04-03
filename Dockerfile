# ---------- Stage 1: Build the JAR ----------
FROM gradle:8.7-jdk21 AS build
WORKDIR /app

# Copy only Gradle config first (for caching dependencies)
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Download dependencies (cache layer)
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src ./src

# Build JAR
RUN ./gradlew clean build -x test --no-daemon


# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Spring Boot default port
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
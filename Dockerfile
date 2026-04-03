# ---------- Stage 1: Build the JAR ----------
FROM gradle:8.7-jdk21 AS build
WORKDIR /app

# Copy Gradle wrapper files
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# ✅ Fix permission issue
RUN chmod +x gradlew

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy source
COPY src ./src

# Build JAR
RUN ./gradlew clean build -x test --no-daemon


# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
# Stage 1: Build stage
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copy gradle executable and wrapper
COPY gradlew .
COPY gradle gradle

# Copy build files to cache dependencies
COPY build.gradle settings.gradle ./

# Give execution permission and download dependencies (layer caching)
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

# Copy the rest of the source code and build the jar
COPY src src
RUN ./gradlew bootJar --no-daemon

# Stage 2: Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Create a non-root user for security
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring

# Copy the jar from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
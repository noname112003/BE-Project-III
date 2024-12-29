# Stage 1: Build the application
FROM gradle:7.6.0-jdk17 AS build
WORKDIR /app
# Copy only Gradle configuration files to cache dependencies first
COPY build.gradle settings.gradle ./
RUN gradle build -x test --no-daemon || return 0

# Copy the rest of the project files
COPY . .
# Build the application, skipping tests
RUN gradle clean build -x test --no-daemon

# Stage 2: Create the runtime image
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
# Copy the JAR file from the build stage to the runtime image
COPY --from=build /app/build/libs/Mock_Project-0.0.1-SNAPSHOT.jar app.jar
# Expose the application port
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

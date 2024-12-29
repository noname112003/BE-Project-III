# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
# Copy all project files to the build container
COPY . .
# Run Maven to clean and build the project, skipping tests
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
# Copy the JAR file from the build stage to the runtime image
COPY --from=build /app/build/libs/Mock_Project-0.0.1-SNAPSHOT.jar app.jar
# Expose the application port
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

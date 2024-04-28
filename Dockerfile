# Stage 1: Build the application
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
# Copy the project files to the container
COPY . .
# Build the application
RUN mvn clean package

# Stage 2: Create the final Docker image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar /app/application.jar
# Run the application
CMD ["java", "-jar", "application.jar"]
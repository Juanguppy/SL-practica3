# Use the official gradle image to create a build artifact.
FROM gradle:8.5.0-jdk17 AS build

# Copy the source code to the container
COPY . /home/gradle/src

# Set the working directory
WORKDIR /home/gradle/src

# Build the application
RUN gradle build --no-daemon 

# Use OpenJDK for runtime
FROM openjdk:17-jdk

# Set the working directory
WORKDIR /app

# Copy the jar to the production image from the builder stage.
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-app.jar

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/app/spring-app.jar"]
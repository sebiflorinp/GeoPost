FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the project JAR file into the container at /app
COPY target/GeoPostAPI-0.0.1-SNAPSHOT.jar /app/geopostapi.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "geopostapi.jar"]
FROM openjdk:21-slim

# Set the Spring profile as an environment variable
ENV SPRING_PROFILES_ACTIVE=dev

WORKDIR /app

# Copy the JAR file into the Docker image
COPY build/libs/*.jar droni-backend.jar

# Set the entry point to run the JAR file with the Spring profile
ENTRYPOINT ["java", "-jar", "/app/droni-backend.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
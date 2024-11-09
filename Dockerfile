FROM eclipse-temurin:21-jre-jammy-minimal

# Set the Spring profile as an environment variable
ENV SPRING_PROFILES_ACTIVE=dev

# Copy the JAR file into the Docker image
COPY build/libs/*.jar /app/droni-backend.jar

# Set the entry point to run the JAR file with the Spring profile
ENTRYPOINT ["java", "-jar", "/app/droni-backend.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
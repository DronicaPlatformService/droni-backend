FROM openjdk:21

COPY build/libs/*.jar /app/droni-backend.jar

ENTRYPOINT ["java", "-jar", "/app/backend.jar"]

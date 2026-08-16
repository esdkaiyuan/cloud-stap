FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the fat jar (adjust the name to match your build output)
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Create data directory for H2
RUN mkdir -p /app/data

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

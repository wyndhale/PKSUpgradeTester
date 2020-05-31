FROM openjdk:8-jdk-alpine
ARG JAR_FILE=PKSUpgradeTester-1.0.${BUILD_NUMBER}.jar
COPY target/${JAR_FILE} app.jar

# Use an external config file
COPY src/main/resources/docker-application.yaml /app/docker-application.yaml
# The environment variable used by spring to reference the external file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.config.location=file:///app/docker-application.yaml"]

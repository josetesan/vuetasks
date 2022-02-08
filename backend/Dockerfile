FROM openjdk:11-oracle

WORKDIR /opt

COPY target/backend-1.0.jar /opt/backend.jar
COPY backend-docker.yml /opt/config.yml

EXPOSE 8080
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/opt/backend.jar", "server", "/opt/config.yml"]

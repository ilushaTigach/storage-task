
FROM openjdk:21-jdk-slim
ADD  storage-service-domain/target/storage-service-domain-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
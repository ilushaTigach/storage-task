FROM openjdk:21-jdk-slim
ADD  target/storage-task-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
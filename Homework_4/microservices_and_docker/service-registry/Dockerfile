FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/service-registry-0.0.1-SNAPSHOT.jar /app/service-registry.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/app/service-registry.jar"]

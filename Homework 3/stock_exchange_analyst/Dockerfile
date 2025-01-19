FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src/
RUN mvn clean package -DskipTests

FROM python:3.12-slim AS python-env
WORKDIR /app
COPY requirements.txt .
RUN pip install --upgrade pip && pip install -r requirements.txt

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar ./app.jar
COPY --from=build /app/src/main/resources ./src/main/resources/
COPY --from=python-env /usr/local/lib/python3.12/site-packages /usr/local/lib/python3.12/site-packages
COPY --from=python-env /usr/local/bin/python3.12 /usr/local/bin/python3.12
COPY --from=python-env /usr/local/bin/pip3.12 /usr/local/bin/pip3.12

RUN ln -s /usr/local/bin/python3.12 /usr/local/bin/python && \
    ln -s /usr/local/bin/pip3.12 /usr/local/bin/pip && \
    apt-get update && apt-get install -y --no-install-recommends libgomp1 && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "./app.jar"]

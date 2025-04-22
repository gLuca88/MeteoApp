# Stage 1: build con Maven
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .
RUN mvn clean package

# Stage 2: runtime con JRE
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/Meteo-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]
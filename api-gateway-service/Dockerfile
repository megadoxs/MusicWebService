FROM maven:3.9.5-eclipse-temurin-17 AS builder
WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN ./mvnw dependency:go-offline

COPY . .
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
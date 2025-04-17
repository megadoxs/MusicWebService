FROM maven:3.9.5-eclipse-temurin-17 AS builder
WORKDIR /build

# Copy pom and source code
COPY pom.xml .
COPY src ./src

# Download dependencies and build the JAR
RUN mvn clean package -DskipTests

# Use slim JDK to run the app
FROM eclipse-temurin:17-jdk
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]

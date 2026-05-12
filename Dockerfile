# ---- Build Stage ----
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B
COPY src ./src
RUN ./mvnw package -DskipTests -B

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Force the keystore path via JVM arguments
EXPOSE 8443
ENTRYPOINT ["java", "-Dserver.ssl.key-store=/app/keystore.p12", "-Dserver.ssl.key-store-password=changeit", "-Dserver.ssl.key-alias=ems-backend", "-Dserver.ssl.key-store-type=PKCS12", "-jar", "app.jar"]

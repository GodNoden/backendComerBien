# Usa una imagen con Maven y JDK 21 para construir
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Copia el código fuente
COPY . .

# Construye el JAR (omite tests para deploy rápido)
RUN mvn clean package -DskipTests

# Imagen final más liviana con Java 21
FROM eclipse-temurin:21-jre-alpine

# Copia el JAR construido
COPY --from=build /target/backend-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto que usa Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
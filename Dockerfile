# Usa una imagen con Maven y JDK 17 para construir
FROM maven:3.8.5-openjdk-21 AS build

# Copia el código fuente
COPY . .

# Construye el JAR (omite tests para deploy rápido)
RUN mvn clean package -DskipTests

# Imagen final más liviana
FROM openjdk:21-jdk-slim

# Copia el JAR construido
COPY --from=build /target/backend-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto que usa Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
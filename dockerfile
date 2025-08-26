# Usa imagen oficial de OpenJDK 17
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo
WORKDIR /app

# Copia el archivo jar (ajusta el nombre si usas otro)
COPY target/araculture-backend.jar ./app.jar

# Expone puerto 8080
EXPOSE 8080

# Ejecuta la app
ENTRYPOINT ["java", "-jar", "app.jar"]

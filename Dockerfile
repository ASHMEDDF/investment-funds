# Usar una imagen de Java 17 para correr la aplicación
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el archivo JAR de tu aplicación en el contenedor
COPY target/funds-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto que usa tu aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
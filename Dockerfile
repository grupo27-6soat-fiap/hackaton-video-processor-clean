# Etapa 1: Construção do JAR
FROM gradle:8.2.1-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# Etapa 2: Imagem final para execução
FROM eclipse-temurin:17-jdk
WORKDIR /app
RUN apt-get update && apt-get install -y ffmpeg

COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]

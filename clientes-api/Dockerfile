# Stage 1: compila o JAR
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /src
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package \
	&& mv target/clientes-api.jar /src/app.jar

# Stage 2: imagem final enxuta
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl && \
	addgroup -g 1001 -S appgroup && \
	adduser -u 1001 -S appuser -G appgroup

COPY --from=build /src/app.jar app.jar
RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

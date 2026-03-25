# =========================
# 🧩 Multi-stage Dockerfile (dev & prod)
# =========================
ARG MODULE=config-service
ARG MODE=prod

# =========================
# Stage 1 — Build (Maven)
# =========================
FROM maven:3.9.6-eclipse-temurin-21 AS builder
ARG MODULE=config-service
WORKDIR /build
COPY . .
RUN mvn -pl ${MODULE} -am clean package -DskipTests

# =========================
# Stage 2 — Runtime
# =========================
FROM eclipse-temurin:21-jdk-alpine
ARG MODULE=config-service
ARG MODE=prod
WORKDIR /app

COPY --from=builder /build/${MODULE}/target/${MODULE}-1.0.0.jar /app/app.jar

ENV SPRING_PROFILES_ACTIVE=${MODE}

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]

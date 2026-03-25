# =========================
# 🧩 Multi-stage Dockerfile (dev & prod)
# =========================
ARG MODE=prod
ARG MODULE=app

# =========================
# Stage 1 — Build for prod
# =========================
FROM maven:3.9.6-eclipse-temurin-21 AS builder
ARG MODULE
WORKDIR /build
COPY . .
RUN mvn -pl ${MODULE} -am clean package -DskipTests

# =========================
# Stage 2 — Runtime base (JDK only)
# =========================
FROM eclipse-temurin:21-jdk-alpine
ARG MODULE
ARG MODE
WORKDIR /app

# نسخ الـ jar الناتج من الـ build stage
COPY --from=builder /build/${MODULE}/target/*.jar /app/app.jar

# بيئة التشغيل العامة
ENV SPRING_PROFILES_ACTIVE=${MODE}
ENV SPRING_DEVTOOLS_RESTART_ENABLED=false
ENV SPRING_DEVTOOLS_LIVERELOAD_ENABLED=false

# فتح المنفذ الافتراضي
EXPOSE 8761

# في وضع dev → شغل بـ Maven
# في وضع prod → شغل بالـ jar
ENTRYPOINT ["/bin/sh", "-c", "\
if [ \"$MODE\" = \"dev\" ]; then \
  cd /app/${MODULE} && mvn spring-boot:run -Dspring-boot.run.profiles=dev; \
else \
  java -jar app.jar; \
fi"]


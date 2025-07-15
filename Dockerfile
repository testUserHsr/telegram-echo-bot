FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /app
COPY target/telegram-echo-bot-*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./

ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75"
EXPOSE 8088
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /app
COPY . /app/.
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/employee_manager-0.0.1.jar /app/employee_manager-0.0.1.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "/app/employee_manager-0.0.1.jar"]
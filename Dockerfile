FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY . .

RUN mkdir -p src/main/resources
RUN cp application.properties src/main/resources/application.properties || echo "file exist"

RUN mvn clean package -Dmaven.test.skip=true -DfailIfNoTests=false
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
FROM gradle:jdk21 AS build
WORKDIR /app



COPY build.gradle settings.gradle ./
COPY gradle ./gradle

COPY src ./src

RUN gradle bootJar --no-daemon -x test

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
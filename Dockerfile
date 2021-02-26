FROM maven:3-adoptopenjdk-15-openj9 AS build-env
WORKDIR /app

COPY . ./

RUN mvn clean install

FROM openjdk:15.0.2

WORKDIR /app

COPY --from=build-env /app/target/RESTful-1.0-SNAPSHOT-jar-with-dependencies.jar .

CMD ["java", "-jar", "RESTful-1.0-SNAPSHOT-jar-with-dependencies.jar"]

EXPOSE 8080/tcp
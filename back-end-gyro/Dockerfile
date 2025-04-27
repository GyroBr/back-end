FROM maven:3.8.4-amazoncorretto-17 AS build

WORKDIR /app

COPY pom.xml ./

RUN mkdir -p /root/.m2 && mvn dependency:go-offline

COPY . .

RUN mvn clean package -DskipTests

FROM amazoncorretto:17

WORKDIR /app

COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

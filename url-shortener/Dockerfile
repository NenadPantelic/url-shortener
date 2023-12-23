FROM maven:3.8-openjdk-17 as builder
WORKDIR /usr/src/url-shortener
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY . .
# we don't need tests when building an image, especially since we have repository tests that connect to DB
RUN mvn clean package -DskipTests=true

FROM amazoncorretto:17-alpine-jdk
MAINTAINER nenadpantelickg@gmail.com
COPY --from=builder /usr/src/url-shortener/target/url-shortener-0.0.1-SNAPSHOT.jar url-shortener-0.0.1.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod","url-shortener-0.0.1.jar"]
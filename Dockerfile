# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn package -DskipTests -q

# Stage 2: Run
FROM tomcat:8.5-jre17
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/servi.war /usr/local/tomcat/webapps/servi.war
EXPOSE 8080
CMD ["catalina.sh", "run"]

# Build phase
FROM maven:3.8.6-eclipse-temurin-17-alpine as MAVEN_BUILD
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn install -DskipTests=true

# Run phase
FROM eclipse-temurin:17.0.5_8-jre-alpine
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/premiere-back.jar /app/
EXPOSE 8080
ENTRYPOINT ["java","-jar","premiere-back.jar"]
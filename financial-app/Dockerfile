FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
# CHANGED: Gradle puts jars in build/libs/, not target/
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
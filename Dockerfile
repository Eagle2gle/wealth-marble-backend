FROM eclipse-temurin:11-alpine
COPY ./build/libs/wealth-marble-backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
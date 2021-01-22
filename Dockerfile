FROM openjdk:11
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} lime-auth.jar
ENTRYPOINT ["java", "-jar", "/lime-auth.jar"]
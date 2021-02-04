FROM openjdk:11
RUN groupadd spring
RUN useradd -g spring spring
USER spring:spring
ARG JAR_FILE=bin/*.jar
COPY ${JAR_FILE} lime-auth.jar
ENTRYPOINT ["java", "-jar", "/lime-auth.jar"]
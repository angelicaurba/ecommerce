FROM openjdk:11-jre-slim
VOLUME /tmp
ARG JAR_FILE=build/libs/*SNAPSHOT.jar
COPY ${JAR_FILE} mailserver.jar

EXPOSE 8500

## RUN addgroup -S app && adduser -S app -G app
## USER app

ENTRYPOINT ["java","-jar","/mailserver.jar"]
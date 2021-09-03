FROM openjdk:11-jre-slim
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} warehouseservice.jar

EXPOSE 8100

## RUN addgroup -S app && adduser -S app -G app
## USER app

ENTRYPOINT ["java","-jar","/warehouseservice.jar"]
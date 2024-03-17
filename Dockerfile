FROM amazoncorretto:17-alpine-jdk
# Refer to Maven build -> finalName
ARG JAR_FILE=target/iss-location-app.jar

# cd /opt/app
WORKDIR /opt/app

COPY ${JAR_FILE} app.jar

# java -jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]

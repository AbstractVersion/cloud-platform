FROM openjdk:11-jre
MAINTAINER George Fiotakis

VOLUME /tmp

ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","-Dspring.profiles.active=doc -Dspring.cloud.config.uri=http://config-server:8888","com.micro.env.HelloServiceApplication"]

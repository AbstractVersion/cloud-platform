FROM openjdk:11-jre
MAINTAINER Cassio Mazzochi Molin <cassio@cassiomolin.com>

COPY ./wait_from_cloud_config.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

VOLUME /tmp

ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app



ENTRYPOINT ["/entrypoint.sh", "java","-cp","app:app/lib/*","-Dspring.profiles.active=docker","com.eureka.server.demo.DemoApplication"]



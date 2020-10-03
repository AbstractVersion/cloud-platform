FROM openjdk:11-jre
Label MAINTEINER="George Fiotakis g.fiotakis@hotmail.com"

COPY ./wait_from_cloud_config.sh /wait_from_cloud_config.sh
RUN chmod +x /wait_from_cloud_config.sh

VOLUME /tmp

ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app



ENTRYPOINT ["/wait_from_cloud_config.sh", "java","-cp","app:app/lib/*","-Dspring.profiles.active=docker","cloud.platform.io.TrafficSimulatorServiceApplication"]


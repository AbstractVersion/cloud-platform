# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
FROM openjdk:11-jre
LABEL maintainer "George Fiotakis"

VOLUME /tmp

# Copy the key RSA file for the decryption / encryption 
COPY ./keys /keys
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 8888

ENTRYPOINT ["java","-cp","app:app/lib/*","com.cloud.config.CloudConfigServerApplication"]
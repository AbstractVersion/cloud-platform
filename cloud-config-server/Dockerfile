# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
FROM java:8-jdk-alpine

COPY ./target/cloud-config-server-0.0.1-SNAPSHOT.jar /tmp/spring-cloud-config-server.jar

EXPOSE 8888

ENTRYPOINT ["sh","-c","java -jar /tmp/spring-cloud-config-server.jar"]


# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
FROM java:8-jdk-alpine

COPY ./target/eureka-server-0.0.1-SNAPSHOT.jar /tmp/name-server.jar

EXPOSE 8761

ENTRYPOINT ["sh","-c","java -jar -Dspring.profiles.active=doc -Dspring.cloud.config.uri=http://config:8888 /tmp/name-server.jar"]

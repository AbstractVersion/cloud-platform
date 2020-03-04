# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
FROM java:8-jdk-alpine

COPY ./target/gateway-api-zuul-0.1.jar /tmp/name-server.jar

EXPOSE 8761

ENTRYPOINT ["sh","-c","java -jar /tmp/name-server.jar"]

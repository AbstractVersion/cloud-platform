# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
FROM java:8-jdk-alpine

COPY ./target/hello-service-0.0.1-SNAPSHOT.jar /tmp/hello-service.jar

EXPOSE 8080

ENTRYPOINT ["sh","-c","java -jar /tmp/hello-service.jar"]

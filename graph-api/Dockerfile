# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
FROM java:8-jdk-alpine

COPY ./target/graph-api-0.0.1-SNAPSHOT.jar /tmp/graph-api-v-0.1.jar

EXPOSE 8080

ENTRYPOINT ["sh","-c","java -jar /tmp/graph-api-v-0.1.jar"]

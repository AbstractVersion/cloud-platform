#!/bin/sh
# This is a comment!

read -p "Enter registry dns: "  registry_ip

echo $registry_ip

echo -------------------Pulling base images---------------------------

docker image pull wurstmeister/zookeeper:latest
docker image tag wurstmeister/zookeeper:latest $registry_ip/zookeeper:latest
docker image push $registry_ip/zookeeper:latest

docker image pull dockersamples/visualizer
docker image tag dockersamples/visualizer $registry_ip/visualizer:latest
docker image push $registry_ip/visualizer:latest


docker image pull wurstmeister/kafka:latest
docker image tag wurstmeister/kafka:latest $registry_ip/kafka:latest
docker image push $registry_ip/kafka:latest

docker image pull mongo:latest
docker image tag mongo:latest $registry_ip/mongo:latest
docker image push $registry_ip/mongo:latest

docker image pull mariadb:latest
docker image tag mariadb:latest $registry_ip/mariadb:latest
docker image push $registry_ip/mariadb:latest

docker image pull rabbitmq:3-management
docker image tag rabbitmq:3-management $registry_ip/rabbitmq:3-management
docker image push $registry_ip/rabbitmq:3-management

docker image pull docker.elastic.co/elasticsearch/elasticsearch:7.2.0
docker image tag docker.elastic.co/elasticsearch/elasticsearch:7.2.0 $registry_ip/elasticsearch:latest
docker image push $registry_ip/elasticsearch:latest



docker image pull docker.elastic.co/logstash/logstash:7.2.0
docker image tag docker.elastic.co/logstash/logstash:7.2.0 $registry_ip/logstash:latest
docker image push $registry_ip/logstash:latest

docker image pull docker.elastic.co/kibana/kibana:7.2.0
docker image tag docker.elastic.co/kibana/kibana:7.2.0 $registry_ip/kibana:latest
docker image push $registry_ip/kibana:latest

docker image pull docker.elastic.co/beats/filebeat:7.2.0
docker image tag docker.elastic.co/beats/filebeat:7.2.0 $registry_ip/beats:latest
docker image push $registry_ip/beats:latest

# Cloud config build
echo Cloud config build
cd cloud-config-server && mvn clean install -DskipTests
# push the image to a local repo
docker push $registry_ip/config-server:latest

# Eurika Server Build
echo Eurika Server build
cd ../eureka-server && mvn clean install -DskipTests
# push the image to a local repo
docker push $registry_ip/discovery-service:latest


# Graph API Build
echo Graph API Build
cd ../graph-api && mvn clean install -DskipTests
# push the image to a local repo
docker push $registry_ip/graph-api:latest

# hello-client Build
echo hello-client Build
cd ../hello-client && mvn clean install -DskipTests
# push the image to a local repo
docker push $registry_ip/hello-client:latest

# hello-service Build
echo hello-service Build
cd ../hello-service && mvn clean install -DskipTests
# push the image to a local repo
docker push $registry_ip/hello-service:latest

# session-service Build
echo session-service Build
cd ../session-service && mvn clean install -DskipTests
# push the image to a local repo
docker push $registry_ip/session-service:latest


# zuul-gateway 
echo -------------------------zuul-gateway Build--------------------------
cd ../zuul-gateway && mvn clean install -DskipTests
# push the image to a local repo
docker push $registry_ip/zuul-proxy:latest

# consumer-service
echo ------------------------- swarm-kafka/producer-service Build--------------------------
cd ../kafka/producer-service && mvn clean install -DskipTests
# push the image to a local repo
docker push $registry_ip/producer-service:latest
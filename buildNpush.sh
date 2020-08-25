#!/bin/sh
# This is a comment!

read -p "Enter registry dns: "  registry_ip

echo $registry_ip

echo -------------------Pulling base images---------------------------

read -p "Pull externall images & push them to the private repository ? (y/n) " RESP
if [ "$RESP" = "y" ]; then
    docker image pull mariadb:latest
    docker image tag mariadb:latest $registry_ip/mariadb:latest
    docker image push $registry_ip/mariadb:latest

    docker image pull rabbitmq:3-management
    docker image tag rabbitmq:3-management $registry_ip/rabbitmq:production
    docker image push $registry_ip/rabbitmq:production

    docker image pull docker.elastic.co/elasticsearch/elasticsearch:7.2.0
    docker image tag docker.elastic.co/elasticsearch/elasticsearch:7.2.0 $registry_ip/elasticsearch:production
    docker image push $registry_ip/elasticsearch:production



    docker image pull docker.elastic.co/logstash/logstash:7.2.0
    docker image tag docker.elastic.co/logstash/logstash:7.2.0 $registry_ip/logstash:production
    docker image push $registry_ip/logstash:production

    docker image pull docker.elastic.co/kibana/kibana:7.2.0
    docker image tag docker.elastic.co/kibana/kibana:7.2.0 $registry_ip/kibana:production
    docker image push $registry_ip/kibana:production

    docker image pull docker.elastic.co/beats/filebeat:7.2.0
    docker image tag docker.elastic.co/beats/filebeat:7.2.0 $registry_ip/beats:production
    docker image push $registry_ip/beats:production

else
    echo "Ok then proceeding with the initialization..."
fi
    

read -p "Build & Push  session-db ? (y/n) " RESP
if [ "$RESP" = "y" ]; then
    
    echo Building Session DB
    docker build -t $registry_ip/session-db:production ./maria-db/session-db
    docker image push $registry_ip/session-db:production

else
    echo "Ok then proceeding with the initialization..."
fi


read -p "Build & Push  CLOUD-CONFIG ? (y/n) " RESP
if [ "$RESP" = "y" ]; then
    
    echo Cloud config build
    cd cloud-config-server && mvn clean install -DskipTests
    # push the image to a local repo
    docker tag $registry_ip/config-server:latest $registry_ip/config-server:production
    docker push $registry_ip/config-server:production
    cd ..
else
    echo "Ok then proceeding with the initialization..."
fi


read -p "Build & Push DISCOVERY-SERVICE ? (y/n) " RESP
if [ "$RESP" = "y" ]; then
    
    # Eurika Server Build
    echo Eurika Server build
    cd eureka-server && mvn clean install -DskipTests
    # push the image to a local repo
    docker tag $registry_ip/discovery-service:latest $registry_ip/discovery-service:production
    docker push $registry_ip/discovery-service:production
    cd ..
else
    echo "Ok then proceeding with the initialization..."
fi


read -p "Build & Push GRAPH-SERVICE ? (y/n) " RESP
if [ "$RESP" = "y" ]; then
    
    # Graph API Build
    echo Graph API Build
    cd graph-api && mvn clean install -DskipTests
    # push the image to a local repo
    docker tag $registry_ip/graph-api:latest $registry_ip/graph-api:production
    docker push $registry_ip/graph-api:production
    cd ..
else
    echo "Ok then proceeding with the initialization..."
fi

read -p "Build & Push SESSION-SERVICE ? (y/n) " RESP
if [ "$RESP" = "y" ]; then
    
    # session-service Build
    echo session-service Build
    cd session-service && mvn clean install -DskipTests
    # push the image to a local repo
    docker tag $registry_ip/session-service:latest $registry_ip/session-service:production
    docker push $registry_ip/session-service:production
    cd ..
else
    echo "Ok then proceeding with the initialization..."
fi

read -p "Build & Push ZUUL-GATEWAY ? (y/n) " RESP
if [ "$RESP" = "y" ]; then
    # zuul-gateway 
    echo -------------------------zuul-gateway Build--------------------------
    cd zuul-gateway && mvn clean install -DskipTests
    # push the image to a local repo
    docker tag $registry_ip/zuul-proxy:latest $registry_ip/zuul-proxy:production
    docker push $registry_ip/zuul-proxy:production
    cd ..
else
    echo "Ok then proceeding with the initialization..."
fi


read -p "Build & Push Python-Service ? (y/n) " RESP
if [ "$RESP" = "y" ]; then
    # zuul-gateway 
    echo -------------------------python-service--------------------------
    cd python-asynch/python-api/ && docker build -t $registry_ip/python-service:production .
    # push the image to a local repo
    docker push $registry_ip/python-service:production
    cd ../..
else
    echo "Ok then proceeding with the initialization..."
fi

read -p "Build & Push Celery-Worker ? (y/n) " RESP
if [ "$RESP" = "y" ]; then
    # zuul-gateway 
    echo -------------------------python-service--------------------------
    cd python-asynch/celery-worker/ && docker build -t $registry_ip/celery-worker:production .
    # push the image to a local repo
    docker push $registry_ip/celery-worker:production
    cd ../..
else
    echo "Ok then proceeding with the initialization..."
fi

read -p "Build & Push python-test ? (y/n) " RESP
if [ "$RESP" = "y" ]; then
    # zuul-gateway 
    echo -------------------------python-service--------------------------
    cd python-asynch/python-test/ && docker build -t $registry_ip/python-service:production .
    # push the image to a local repo
    docker push $registry_ip/python-service:production
    cd ../..
else
    echo "Ok then proceeding with the initialization..."
fi


echo "The service build has finished you can now run the docker stack"


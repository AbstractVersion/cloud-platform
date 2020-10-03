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

    docker image pull nginx:alpine
    docker image tag nginx:alpine $registry_ip/nginx:alpine
    docker image push $registry_ip/nginx:alpine

    docker image pull mongo-express
    docker image tag mongo-express $registry_ip/mongo-express
    docker image push $registry_ip/mongo-express


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

read -p "Do you want to run Eurika under high availability mode ? (y/n) " RESP
if [ "$RESP" = "y" ]; then
    read -p "Build & Push DISCOVERY-SERVICE peer-1 node ? (y/n) " RESP
    if [ "$RESP" = "y" ]; then
        
        # Eurika Server Build
        echo Eurika Server-peer1 build
        cd eurika-group/eureka-server-peer1/ && mvn clean install -DskipTests
        # push the image to a local repo
        # docker tag $registry_ip/discovery-service:latest $registry_ip/discovery-service:production
        docker push $registry_ip/discovery-service-peer1:production
        cd ../..
    else
        echo "Ok then proceeding with the initialization..."
    fi

    read -p "Build & Push DISCOVERY-SERVICE peer-2 node ? (y/n) " RESP
    if [ "$RESP" = "y" ]; then
        
        # Eurika Server Build
        echo Eurika Server-peer2 build
        cd eurika-group/eureka-server-peer2/ && mvn clean install -DskipTests
        # push the image to a local repo
        # docker tag $registry_ip/discovery-service:latest $registry_ip/discovery-service:production
        docker push $registry_ip/discovery-service-peer2:production
        cd ../..
    else
        echo "Ok then proceeding with the initialization..."
    fi
    
else
    read -p "Build & Push DISCOVERY-SERVICE single node ? (y/n) " RESP
    if [ "$RESP" = "y" ]; then
        
        # Eurika Server Build
        echo Eurika Server build
        cd eurika-group/eureka-server/ && mvn clean install -DskipTests
        # push the image to a local repo
        # docker tag $registry_ip/discovery-service:latest $registry_ip/discovery-service:production
        docker push $registry_ip/discovery-service:production
        cd ../..
    else
        echo "Ok then proceeding with the initialization..."
    fi
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

read -p "Build & Push mongo-consumer ? (y/n) " RESP
if [ "$RESP" = "y" ]; then
    # zuul-gateway 
    echo -------------------------mongo-consumer--------------------------
    cd kafka/mongo-consumer/ && mvn clean install -DskipTests
    # push the image to a local repo
    docker tag $registry_ip/mongo-comsumer:latest $registry_ip/mongo-comsumer:production
    docker push $registry_ip/mongo-comsumer:production
    cd ../..
else
    echo "Ok then proceeding with the initialization..."
fi

read -p "Build & Push library-producer ? (y/n) " RESP
if [ "$RESP" = "y" ]; then
    # zuul-gateway 
    echo -------------------------library-producer--------------------------
    cd kafka/library-producer/ && mvn clean install -DskipTests
    # push the image to a local repo
    docker tag $registry_ip/library-producer:latest $registry_ip/library-producer:production
    docker push $registry_ip/library-producer:production
    cd ../..
else
    echo "Ok then proceeding with the initialization..."
fi

read -p "Build & Push hello-service ? (y/n) " RESP
if [ "$RESP" = "y" ]; then
    # zuul-gateway 
    echo -------------------------hello-service--------------------------
    cd hello-service && mvn clean install -DskipTests
    # push the image to a local repo
    docker tag $registry_ip/hello-service:latest $registry_ip/hello-service:production
    docker push $registry_ip/hello-service:production
    cd ..
else
    echo "Ok then proceeding with the initialization..."
fi

read -p "Build & Push hello-client ? (y/n) " RESP
if [ "$RESP" = "y" ]; then
    # zuul-gateway 
    echo -------------------------hello-client--------------------------
    cd hello-client && mvn clean install -DskipTests
    # push the image to a local repo
    docker tag $registry_ip/hello-client:latest $registry_ip/hello-client:production
    docker push $registry_ip/hello-client:production
    cd ..
else
    echo "Ok then proceeding with the initialization..."
fi

read -p "Build & Push traffic-simulator-service ? (y/n) " RESP
if [ "$RESP" = "y" ]; then
    # zuul-gateway 
    echo -------------------------traffic-simulator-service--------------------------
    cd traffic-simulator-service && mvn clean install -DskipTests
    # push the image to a local repo
    docker tag $registry_ip/traffic-simulator-service:latest $registry_ip/traffic-simulator-service:production
    docker push $registry_ip/htraffic-simulator-service:production
    cd ..
else
    echo "Ok then proceeding with the initialization..."
fi

echo "The service build has finished you can now run the docker stack"


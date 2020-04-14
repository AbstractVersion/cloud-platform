#!/bin/sh
# This is a comment!

read -p "Enter registry dns: "  registry_ip

echo $registry_ip

# Cloud config build
echo Cloud config build
cd cloud-config-server && mvn clean install -DskipTests
# push the image to a local repo
docker tag micro-env/config-server:latest $registry_ip/config-server:latest
docker push $registry_ip/config-server:latest

# Eurika Server Build
echo Eurika Server build
cd ../eureka-server && mvn clean install -DskipTests
# push the image to a local repo
docker tag micro-env/discovery-service:latest $registry_ip/discovery-service:latest
docker push $registry_ip/discovery-service:latest


# Graph API Build
echo Graph API Build
cd ../graph-api && mvn clean install -DskipTests
# push the image to a local repo
docker tag micro-env/graph-api:latest $registry_ip/graph-api:latest
docker push $registry_ip/graph-api:latest

# hello-client Build
echo hello-client Build
cd ../hello-client && mvn clean install -DskipTests
# push the image to a local repo
docker tag micro-env/hello-client:latest $registry_ip/hello-client:latest
docker push $registry_ip/hello-client:latest

# hello-service Build
echo hello-service Build
cd ../hello-service && mvn clean install -DskipTests
# push the image to a local repo
docker tag micro-env/hello-service:latest $registry_iphello-service:latest
docker push $registry_ip/hello-service:latest

# session-service Build
echo session-service Build
cd ../session-service && mvn clean install -DskipTests
# push the image to a local repo
docker tag micro-env/session-service:latest $registry_ip/session-service:latest
docker push $registry_ip/session-service:latest


# zuul-gateway 
echo -------------------------zuul-gateway Build--------------------------
cd ../zuul-gateway && mvn clean install -DskipTests
# push the image to a local repo
docker tag micro-env/zuul-proxy:latest $registry_ip/zuul-proxy:latest
docker push $registry_ip/zuul-proxy:latest
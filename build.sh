#!/bin/sh
# This is a comment!
echo -------------------configuring essential things------------------


echo Creating directory volumes ...
mkdir -p ./elk-template/filebeat/data
mkdir -p ./elk-template/elasticsearch/data
mkdir -p ./pre-deploy/mariadb/data/

sudo chmod 600 ./elk-template/filebeat/filebeat.docker.yml && sudo chown -R root:root ./elk-template/filebeat/filebeat.docker.yml

echo -------------------Starting Build---------------------

# Cloud config build
echo Cloud config build
cd cloud-config-server && mvn clean install -DskipTests

# Eurika Server Build
echo Eurika Server build
cd ../eureka-server && mvn clean install -DskipTests

# Graph API Build
echo Graph API Build
cd ../graph-api && mvn clean install -DskipTests

# hello-client Build
echo hello-client Build
cd ../hello-client && mvn clean install -DskipTests

# hello-service Build
echo hello-service Build
cd ../hello-service && mvn clean install -DskipTests

# session-service Build
echo session-service Build
cd ../session-service && mvn clean install -DskipTests

# zuul-gateway 
echo -------------------------zuul-gateway Build--------------------------
cd ../zuul-gateway && mvn clean install -DskipTests

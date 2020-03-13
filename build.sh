#!/bin/sh
# This is a comment!
echo -------------------configuring essential things------------------

echo Configuring Filebeat ...
chmod 600 ./filebeat/filebeat.docker.yml && chown -R root:root ./filebeat/filebeat.docker.yml

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

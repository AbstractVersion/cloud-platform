# Micro Enviroment

## Create network

docker network create -d bridge micro-network

## Create Cloud-Config container.

### Build the image
docker build -t  micro/confing-service:latest . 

### Run the container

docker container run -d  --network micro-network --name confing micro/confing-service:latest


## Create Discovery-service container.

### Build the image
docker build -t  micro/discovery-service:latest . 

### Run the container

docker container run -d -p 80:8761 --network micro-network --name discovery-service micro/discovery-service:latest


## Create hello-service.

### Build the image
docker build -t  micro/hello-service:latest . 
docker build -t  micro/hello-service:p-8001 .

### Run the container

docker container run -d --network micro-network --name hello-service micro/hello-service:latest
docker container run -d --network micro-network --name hello-service2 micro/hello-service:p-8001
docker container run -d --network micro-network --name hello-service3 micro/micro/hello-service:p-8001


## Create invocation-client.

### Build the image
docker build -t  micro/hello-client:latest . 
docker build -t  micro/hello-client:p-7878 . 

### Run the container

docker container run -d  --network micro-network --name hello-client micro/hello-client:latest
docker container run -d  --network micro-network --name invoc-service micro/hello-client:p-7878

## Docker for Graph API

### Build the image
docker build -t  micro/graph-service:latest . 

### Run the container
docker container run -d  --network micro-network --name graph-service micro/graph-service:latest


## Create the gateway Service

### Build the image
docker build -t  micro/zulu-proxy:latest . 

### Run the container

docker container run -d -p 8080:8080 --network micro-network --name zuul-proxy micro/zulu-proxy:latest

##Docker for Python Template

### Build the image
docker build -t  micro/python-api:latest . 

### Run the container
docker container run -d  --network micro-network --name python-service micro/python-api:latest


## User Managment

### Create the database volume
docker volume create --driver local \
      --opt type=none \
      --opt device=/mnt/volumes/maria-db/user-db \
      --opt o=bind \
      user-db-volume

### Create the service database
docker run -d --name user-database --network micro-network  -v user-db-volume:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=InfiNite@KK@ mariadb

#####Local version of the DB
docker run -d --name user-database -p 3306:3306 -e MYSQL_ROOT_PASSWORD=InfiNite@KK@ mariadb
#####Local myadmin 
docker run --name myadmin-user -d --link user-database:db -p 8080:80 phpmyadmin/phpmyadmin



## ELK Distributed log tracing with ElasticSearch. 


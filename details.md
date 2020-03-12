# Micro Enviroment

## Permissions on the Elastic && Filebeat.yml
## Create network

docker network create -d bridge micro-network

## Create Cloud-Config container.

### Build the image
docker build -t  micro-env/config-server:latest . 

### Run the container

docker container run -d  --network micro-network --name config-server micro-env/config-server


## User Managment

### Create the database volume
docker volume create --driver local \
      --opt type=none \
      --opt device=/docker/volumes/generalRDBMS \
      --opt o=bind \
      generalRDBMS

### Create the database container
docker run -d --name generalRDBMS --network micro-network  -v generalRDBMS:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=root mariadb

### Create the database itsealf

create database sessionDB;

### Create the session table;

CREATE TABLE `session_info` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token_id` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `access_token` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `expiring` int(11) NOT NULL,
  `date_created` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



## Create Discovery-service container.

### Build the image
docker build -t  micro/discovery-service:latest . 

### Run the container

docker container run -d -p 80:8761 --network micro-network --name discovery-service micro-env/discovery-service:latest


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



## ELK Distributed log tracing with ElasticSearch. 


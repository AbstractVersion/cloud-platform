# Microservice Ecosystem

This document is aiming to demonstrate a microservice general purpose architecture. All the work on this document is open source and is licensed under Apache License.

## Table of Contents
- [Known technologies & patterns](#known-technologies-&-patterns)
  * [Architecture Backbone](#architecture-backbone)
  * [Service Discovery](#service-discovery)  
  * [Environment  & Cloud configuration](#environment-&-cloud-configuration)
  * [Log Aggregation](#log-aggregation)
    + [Elasticsearch](#elasticsearch)
    + [Kibana](#kibana)
    + [Beats](#beats)
    + [Logstash](#logstash)
    + [Putting the pieces together](#putting-the-pieces-together)
  * [Cloud Architecture & DevOps](#cloud-architecture-&-devOps)
    + [Cloud Architecture](#cloud-architecture)
    + [Docker Architecture](#docker-architecture)
    + [Build & deploy pipeline](#build-&-deploy-pipeline)
- [Microservice Architecture](#microservice-architecture)
  * [Request Authentication & JWT Generation.](#request-authentication-&-jwt-generation.)
    + [JWT Generation](#jwt-generation)
    + [Request/JWT Authentication](#request/jwt-authentication)
  * [Abstract use-case](#abstract-use-case)
- [Deploy The System](#deploy-the-system)
  * [Docker Compose](#docker-compose)
  * [Docker Swarn](#docker-swarm)
- [Future Additions](#future-additions)


## Known technologies & patterns
Lets take a look on what component we used and how each one of them solve a microservice architecture "common problem". We will classify the technologies used based on the use-case that they help to implement :

* Architecture Backbone
* Service Discovery 
* Log Aggregation/Centralization
* Real-Time service configuration
* Development & Operations

![Implemented Technologies][technologies-used-diagram]



### Architecture Backbone

We define the backbone of the architecture as the core components that need to be active in order for as to be able to add a use-case (service) on our environment. So, essentially the backbone is a group of services that all together provide the core functionality of the ecosystem. Let's take a look at each piece & framework:

* We use [Apache2 HTTPD](https://httpd.apache.org/) as http proxy to forward the requests to the gateway service & encrypt the traffic (SSL/TLS).
* From [Netflix OSS](https://netflix.github.io/), we use [Zuul](https://github.com/Netflix/zuul) as a gateway service. Zuul acts as an entry point for the rest of the microservice ecosystem. Each request coming from Apache passes through this service. Gatway service performs request authentication & routs the traffic the appropriate service. 
* From [Netflix OSS](https://netflix.github.io/), we use [Eureka ](https://github.com/Netflix/eureka) as discovery service. Eureka enables the microservices to register themselves. Also provides the HTTP address for each service registered.
* From [Netflix OSS](https://netflix.github.io/), we use [Ribbon ](https://github.com/Netflix/eureka) to perform client-side load balancing on the requests performed between microservices.
* We use [Elastic Stack](https://www.elastic.co/products/) & [Docker](https://www.docker.com/) for log aggregation/centralization.
* We use [Spring Cloud](https://spring.io/projects/spring-cloud), [Rabbit MQ](https://www.rabbitmq.com/) & [GitHub](https://github.com/) for environment & attribute configuration.
* For request authentication we provide [JWT](https://oauth.net/2/jwt/) implementation.
* Finally we use [Spring Boot](https://spring.io/projects/spring-framework) & [Flask](https://flask.palletsprojects.com/en/1.1.x/) frameworks for each service base implementation.

![overal-system-backbone][overal-system]


### Service Discovery

Service discovery is a common "nightmare" on all the architectures that are based on microservces. That's because when instead of one or tow monolithic applications, you now have 15, 20, 30, services that each one of them needs to be able to communicate with each other and most important scale independently. Let's take a look at the process :

* Each service register itself with Eureka.
* Ribbon requests the URL list for a specific service, and based on a load balancer algorithm (Round Robbin in this case) forwards the traffic.
* Each service that needs to request information from another microservice uses ribbon to perform the request.
* The gateway service routs the traffic with ribbon as well.

![service-discovery][service-discovery]


### Enviroment & Cloud configuration
Cloud configuration is a very important part of the system. Each service retrieves its configuration from the cloud config server. Let's take a look how:

* Configuration for each service is stored on a git repository.
* Once a service fires up, it requests from the cloud-config server the configuration.
* Every sensitive information is hashed & decrypted at run time with the help of Java Security Framework presented on Java 11 & above.
* Rabbit MQ is a message broker. Spring framework provides a tool called Spring Cloud Bus that allows each service to register as consumer or provider. 
* Cloud config server and all its instances are registered as providers on the RabbitMQ queue.
* Every other microservice registers itself with Spring Cloud Bus as a consumer on the RabbitMQ queue.
* Once an actor performs a push on the remote git repository that holds the configuration information and then pings the endpoint of spring cloud server /bus/refresh. Cloud server will pull the new version of the configurations and let each active service that the configuration has changed. So the service will request the new configuration from the cloud config server. That's how we achieve real time configuration.

![Service configuration][service-configuration]

### Log Aggregation
In a microservices architecture, a single business operation might trigger a chain of downstream microservice calls, which can be pretty challenging to debug. Things, however, can be easier when the logs of all microservices are centralized and each log event contains details that allow us to trace the interactions between the applications.

This is where Elastic Stack comes int picture. Elastic Stack is a group of open source applications from Elastic designed to take data from any source and in any format and then search, analyze, and visualize that data in real time. It was formerly known as ELK Stack, in which the letters in the name stood for the applications in the group: Elasticsearch, Logstash and Kibana. A fourth application, Beats, was subsequently added to the stack, rendering the potential acronym to be unpronounceable. So ELK Stack became Elastic Stack.

##### Elasticsearch
Elasticsearch is a real-time, distributed storage, JSON-based search, and analytics engine designed for horizontal scalability, maximum reliability, and easy management. It can be used for many purposes, but one context where it excels is indexing streams of semi-structured data, such as logs or decoded network packets.

##### Kibana
Kibana is an open source analytics and visualization platform designed to work with Elasticsearch. Kibana can be used to search, view, and interact with data stored in Elasticsearch indices, allowing advanced data analysis and visualizing data in a variety of charts, tables, and maps.

##### Beats
Beats are open source data shippers that can be installed as agents on servers to send operational data directly to Elasticsearch or via Logstash, where it can be further processed and enhanced. There’s a number of Beats for different purposes:

* Filebeat: Log files
* Metricbeat: Metrics
* Packetbeat: Network data
* Heartbeat: Uptime monitoring
* And more.

As we intend to ship log files, Filebeat will be our choice.

##### Logstash
Logstash is a powerful tool that integrates with a wide variety of deployments. It offers a large selection of plugins to help you parse, enrich, transform, and buffer data from a variety of sources. If the data requires additional processing that is not available in Beats, then Logstash can be added to the deployment.

##### Putting the pieces together
The following illustration shows how the components of Elastic Stack interact with each other:

![centralized-logging][centralized-logs]

In a few words:

* Each service is deployed on a docker container. Docker writes the logs on each host machine at /vat/lib/docker/containers... directory.
* Filebeat is deployied on each host machine (docker worker) and collects data from the log files and sends it to Logststash.
* Logstash enhances the data and sends it to Elasticsearch. (Docker Swarm Level)
* Elasticsearch stores and indexes the data. (Docker Swarm Level)
* Kibana displays the data stored in Elasticsearch. (Docker Swarm Level)

### Cloud Architecture & DevOps.

Since we have everything else figured out now we need to see how and where do we deploy the system. For that we have used Azure Cloud, Debian 10, Docker Engine, Docker Swarm, Git & Maven.

#### Cloud Architecture
* On Azure cloud Subscription we have created a virtual network with tow subnets: FrontEnd and BackEnd subnet.
* FrontEnd Subnet is used to proxy the traffic from and to the BackEnd subnet. Each subnet has its own Network Security Group (NSG) that restricts the traffic that comes in and off the subnet.
* On the FrontEnd Subnet there is a proxy machine that acts as the entry point of the hole ecosystem. On this machine we have an Apache HTTPD server that is configured to act as a static proxy that redirects the traffic from the WWW to the gateway API's. This Apache is also configured to encrypt/decrypt the traffic based on the HTTPS protocol SSL/TLS.
* On the backend subnet we have a set of workers (pods on Kubernets) which pretty much are host machines in which we have installed Docker Engine.
* Docker Swarm as container orchestration to bring all these engines together and work as one.

![cloud-architecture][resource-architecture]


#### Docker Architecture
We spoke of the cloud architecture and how the different virtual machines communicate with each other. So, it's time to take a look at the proposed docker architecture for the presented ecosystem. Let's break it into pieces:

* First of all we use Docker Swarm because it is not a large scale project.
* We create an overlay network with tow subnets. A front-end and a back-end.
* We have 3 host machines running Debian or every other Linux distro. On this machine we install docker Engine & enable the Docker Swarm package.
* As swarm manager we define one of the 3 docker engine. On this engine we run the services that do not need to scale as much as the microservices. For instance ELK, Cloud Config & the databases have no meaning to be on each worker. (That's up to you though how you will organize the service deployment).
* We register the tow other engines as workers on our swarm cluster. Which means that on those tow engines we deploy and scale our system. In this particular case because the system is not enormous we just use the different engines to replicate the services. Se, each engine has a copy of the rest of the environment.
* The deployment and the container orchestration happens with Swarm Stack.
* Each service communicate with each other through the overlay network of the Swarm Cluster.
* All the gateway, business & CRUD services are attached on the front-end network.
* While all the database & sensitive repos are attached on the back-end network. On the back-end network are attached the CRUD services also that needs to communicate with this layer.

![docker-architecture][docker-architecture]


#### Build & deploy pipeline
* Since docker is used to package & deploy each service we have made a base implementation of the Spotify library to automate the image build for each service with Apache Maven.

* Git is used for the project versioning and for the build pipeline.

* For the python Flask services we have made a base image which can be extended. On this base image we deploy the python services. 



## Microservice Architecture
Now that we have covered the basics of the ecosystem, it's time to take a closer look at the ecosystem's architecture itself.

### Request Authentication & JWT Generation.
The system provides 2 different types of authentication. An SSO based version of authentication based on Azure Active directory and a custom authentication based on a custom user database.

### JWT Generation
We have tow different ways of authentication thus we have tow different ways of generating jwt tokens (with different signatures). For simplicity we will explain in depth only the SSO version of the JWT token generation. The process of JWT token generation is presented on the following UML process diagram.

![token-registration][token-registration]

#### Request/JWT Authentication
For each different authentication mechanism we use a different gateway API. This way we can demonstrate the real value that an architecture such as this provides. Let's break it down to pieces.

![toke-authentication-flow][toke-authentication-flow]


##### SSO token authentication request flow
1. A request with a JWT token arrives at the Apache WebServer. Depending on the sub-url we redirect the traffic to the appropriate gateway API.

2. The gateway API performs token validation (over the token signature) and checks if the token is expired. 

3. Next the gateway API extracts the user information from the JWT token and requests information verification from the session-service.

4. Session-service depending on the header and the data-stracture requests the graph-service to validate the user information. It retrieves the Microsoft token from the session-database (based on the user UUID) and implements the Microsoft token to the body of the request on graph-service.

5. Graph-service receives the request, extracts the Microsoft token from the body and requests Microsoft Graph API for information based on this token. Microsoft Graph API returns the user information if the token is valid. (We use Microsoft Graph as a Real time user database among others.). The response is the structured and returned to the session service

6. If the user exists, and he is active a new session is registered on the Session-service, which is written in the session-db.

7. The session DB is a NoSQL database (Mongo) because we store data of the same family (session) but with different structure (SSO, custom).

8. The session information reaches the gateway-service in which the final stage of authentication takes palace, cross validate the information we got from the session-service with the ones from the token we received from the request.

9. After the authentication the gateway service checks if the user has the authority to ping the requested service and forwards the traffic to appropriate service.

##### Custom (User) token authentication request flow
The process stays the same through the custom authentication with the one described above. The only difference is on the steps 4, 5 where the system instead of contacting the Microsoft Graph for token exchange it contacts the user service.

4. Session-service depending on the header and the data-stracture requests the user information from the user-service bases on the user UUID.

5. User-service requests the user information based on the User UUID from the user database and returns the outcome to the session service.




### Abstract use-case

Since we have made it this far, lets take a look at one of the benefits that this architecture provides.

* Individual and independent scale for each service.

* Individual test & deploy pipeline for each service.

* Technology Independence.

* Reactive programming with implementation  of Message brokers.

* Code reusability

* Different entrypoints for each business  model.

![abstract-use-case][abstract-use-case]


## Deploy the System

### Docker Compose 

### Docker Swarn

## Future Additions

[technologies-used-diagram]: ./img/architecture/general-architecture-components.jpg "Implemented Technologies"

[service-configuration]: ./img/architecture/enviroment-cloud-config.jpg "Service configuration"

[centralized-logs]: ./img/architecture/log-ggrecation-no-bg.jpg "centralized-logging"

[resource-architecture]: ./img/architecture/resource-architecture.jpg "resource-architecture"

[toke-authentication-flow]: ./img/architecture/authentication-flow.jpg "toke-authentication-flow"

[token-registration]: ./img/architecture/token-registration.jpg "token-registration"
[abstract-use-case]: ./img/architecture/request-flow-example.jpg "abstract-use-case"
[service-discovery]: ./img/architecture/service-discovery.jpg "service-discovery"
[overal-system]: ./img/architecture/overal-core-system.jpg "overal-system-backbone"
[docker-architecture]: ./img/architecture/docker-architecture.jpg "docker-architecture"


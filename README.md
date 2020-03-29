# Microservice Ecosystem

This document is aiming to demonstrate a microserice general purpose architecture. All the work on this document is opensource and is licenced under Apache Licence.

## Table of Contnents
- [Known technologies & patterns](#known-technologies-&-patterns)
  * [Service Discovery](#service-discovery)
  * [Architecture Backbone](#architecture-backbone)
  * [Enviroment & Cloud configuration](#enviroment-&-cloud-configuration)
  * [Log Aggrication](#log-aggrication)
    + [Elasticsearch](#elasticsearch)
    + [Kibana](#kibana)
    + [Beats](#beats)
    + [Logstash](#logstash)
    + [Putting the pieces together](#putting-the-pieces-together)
  * [Resource Architecture & DevOps](#resource-architecture-&-devOps)
    + [Resource Architecture](#resource-architecture)
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

* Service Discovery & Architecture Backbone
* Log Aggrication/Sentrilization
* Real-Time service configuration
* Development & Operations

![Implemented Technologies][technologies-used-diagram]

### Service Discovery

Service discovery is a common "nightmare" on all the architectures that are based on microservces. Thats because when instead of one or tow monolithic applications, you now have 15, 20, 30, servces that each one of them needs to be able to communicate with each other and monst important scale indepentandly. The present architecture is not focusing on a custom solution of service discovery thus we have used the very handfull solution provided by Neftlix OSS. Netflix has developed a number of projects that you might find usefull on architectures such as this. Most importantly this software is tested, validated and implemented, by Netflix which means that is fault tollerant and trustworthy. You can find more infomrations [here](https://netflix.github.io/)

![service-discovery][service-discovery]

### Architecture Backbone

Now that we know how to discover our services, we need a framework in which we can work on and implement the Netflix OSS. Luckily for us one of the biggest frameworks wright now, does just this. Spring framework has been evolved to an amaizing tool for situations like this. [Spring](https://spring.io/projects/spring-boot) provides easy-to-use implementations for all the components we need such as Netflix OSS, but also provides it's own very helpfull set of tools such as [spring-cloud](https://spring.io/projects/spring-cloud), [spring-data](https://spring.io/projects/spring-data) etc. Spring though is a java based framework and we need more than that in order to prove the flexibility and show the real benefits of this architecture. Thats why we have chosen [Flask](https://flask.palletsprojects.com/en/1.1.x/) as the secondary framework on which we can deploy our applications and be compatitable with the rest of the ecosystem.

![overal-system-backbone][overal-system]

### Enviroment & Cloud configuration
Since we have diffined the backbone of our architectures (languages, frameworks etc.), it's time for us to take a look at the different enviroment configurations & the way we will be injecting configurations into our system. An architecture such as this provides a lot of beneffits and a variaty of different wasy that you can implement you architecure ideas. In our case for service configuration we have used Spring Cloud, Spring Cloud Bus, Git & Rabbit MQ.

![Service configuration][service-configuration]

### Log Aggrication
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

* Each service is deployied on a docker container. Docker writes the logs on each host machine at /vat/lib/docker/containers... directory.
* Filebeat is deployied on each host machine (docker worker) and collects data from the log files and sends it to Logststash.
* Logstash enhances the data and sends it to Elasticsearch. (Docker Swarm Level)
* Elasticsearch stores and indexes the data. (Docker Swarm Level)
* Kibana displays the data stored in Elasticsearch. (Docker Swarm Level)

### Resource Architecture & DevOps.

Since we have everything else figured out now we need to see how and where do we deploy the system. For that we have used Azure Cloud, Debian 10, Docker Engine, Docker Swarm, Git & Maven.

Let's take a closer look and brake it down to pieces.

#### Resource Architecture
* On Azoure cloud Subscription we have created a vritual network with tow subnets: FrontEnd and BackEnd subnet.
* FrontEnd Subnet is used to proxy the traffic from and to the BackEnd subnet. Each subnet has it's own Network Security Group (NSG) that restricts the traffic that comes in and off the subnet.
* On the FrontEnd Subnet there is a proxy machine that acts as the entrypoint of the hole ecosystem. On this machine we have an Apache HTTPD server that is configured to act as a static proxy that redirects the traffic from the WWW to the gateway API's. This apache is also configured to encrypt/decrypt the traffic based on the https protocol SSL/TLS.
* On the backend subnet we have a set of workers (pods on Kubernets) which preatty much are host machines in which we have installed Docker Engine.
* Docker Swarm as container orchistration to bring all theese engines together and work as one.

![resource-architecture][resource-architecture]


#### Docker Architecture

![docker-architecture][docker-architecture]


#### Build & deploy pipeline
* Since docker is used to package & deploy each service we have made a base implementation of the Spotify library to automate the image build for each service with Apache Maven.

* Git is used for the project versioning and for the build pipeline.

* For the python Flask services we have made a base image which can be extended. On this base image we deploy the python services. 



## Microservice Architecture
Now that we have covered the basics of the ecosystem, it's time to take a closer look at the ecosystem's architecture itself.

### Request Authentication & JWT Generation.
The system provides 2 different tipes of authentication. An SSO based version of authentication based on Azure Active directory and a custom authentication based on a custom user database.

### JWT Generation
We have tow different ways of authentication thus we have tow different ways of generating jwt tokens (with different signatures). For simplisity we will explain in depth only the SSO version of the JWT token generation. The process of JWT token generation is presented on the followin UML process diagram.

![token-registration][token-registration]

#### Request/JWT Authentication
For each different authentication mechanism we use a different gateway API. This way we can demonstrate the real value that an architecture such as this provides. Lets break it down to pieces.

![toke-authentication-flow][toke-authentication-flow]


##### SSO token authentication request flow
1. A request with a JWT tocken arrives on the Apache WebServer. depending on the sub-url we redirect the traffic to the appropriate gateway API.

2. The gateway API performs token validation (over the token signature) and checks if the token is expired. 

3. Next the gateway API extracts the user information from the JWT token and requests information verification from the session-service.

4.  Session-service depending on the headder and the data-stracture requests the graph-service to validate the user information. It retrieves the microsoft token from the session-database (based on the user uuid) and implements the microsoft token to the body of the request on graph-service.

5.  Graph-service recieves the request, extracts the microsoft token from the body and requests Microsoft Graph API for information based on this token. Microsoft Graph API returns the user information if the token is valid. (We use Microsoft Graph as a Real time user database among others.). The response is the stractured and retruned to the session service

6.  If the user exists and he is active a new session is registered on the Session-service, which is written in the session-db.

7. The session DB is a NoSQL database (Mongo) because we store data of the same fammily (session) but with different structure (SSO, custom).

8. The session information reaches the gateway-service in which the final stage of authentication takes palace, cross validate the information we got from the session-service with the ones from the token we recieved from the request.

9.  After the authentication the gateway service checks if the user has the authority to ping the requested service and forwreds the traffic to appropriate service.

##### Custom (User) token authentication request flow
The process stays the same through the custom authentication with the one discribed above. The only difference is on the steps 4, 5 where the system instead of contacting the Microsoft Graph for token excange it contacts the user service.

4.  Session-service depending on the headder and the data-stracture requests the user information from the user-service bases on the user UUID.

5.  User-service requests the user information based on the User UUID from the user database and returns the outcome to the session service.





### Abstract use-case

Since we have made it this far, lets take a look at one of the benefits that this architecture provides.

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


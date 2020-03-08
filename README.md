# A Microservices Enviroment Deployied on Docker Engine 

>This document's goal is to describe the high-end architecture of the presented microservice ecosystem. The ecosystem currentely supports services written in 2 languages: python & java, but there is the posibility to increase that number uppon request.

<!-- Table of Contetns -->
## Table of Contnents
- [Overview](#Overview)
  * [Technologies-used ](#technologies-used )
    + [Microservices](#microservices)
    + [Docker](#docker)
    + [Spring Netflix/Cloud](#spring-netflix/cloud)
    + [Java & Spring](#java-&-spring)
    + [Python & Flask](#python-&-flask)
    + [Elastic Stack](#elastic-stack)
- [Microservice Architecture](#microservice-architecture)
  * [Service-list & coresponding content](#service-list-&-coresponding-content)
    + [Cloud Configuration Server](#cloud-configuration-server)
    + [Eurika Name Server](#eurika-name-server)
    + [Secrets Service](#secrets-service)
    + [Graph API](#graph-api)
    + [Session Managment](#session-managment)
    + [Hello Service](#hello-service)
    + [Python Service](#python-service)
    + [Hello Client](#hello-client)
    + [Gateway Proxy Service](#gateway-proxy-service)
    + [Elastic Stack](#elastic-stack)
- [Docker Architecture](#docker-architecture)
  * [Docker Containers & Spotofy Maven plugin](#sub-heading-2)
  * [Docker Networks](#docker-networks)
  * [Docker Volumes](#docker-volumes)
- [Run The System](#run-the-system)
  * [Localy](#local-run)
  * [Docker](#docker-run)
- [Future Additions](#future-additions)

<!-- Overview -->
## Overview

Microservices & Docker is tow of the biggest trends of our era used in software industry. This project/template aims to demostrate a base template of a microservice enviroment that can be used in a varriety of different use-cases. This template focuses on :  Microservices Architecture/Implementation & Microservices with Docker Engine. The core "body" of the ecosystem is implemented in java by using the Spring [Cloud](https://spring.io/projects/spring-cloud)/[ Netflix](https://spring.io/projects/spring-cloud-netflix) libraries. 

### Technologies-used 

In this section we will present the core technologies & concepts that the present ecosystem is depending on.


![alt text][logo]

[logo]: ./img/technplogies.jpg "Logo Title Text 2"

#### Microservices
Microservices - also known as the microservice architecture - is an architectural style that structures an application as a collection of services that are :

* Highly maintainable and testable
* Loosely coupled
* Independently deployable
* Organized around business capabilities
* Owned by a small team
* The microservice architecture enables the rapid, frequent and reliable delivery of large, complex applications. It also enables an organization to evolve its   technology stack.

source : [here](https://microservices.io/).
#### Docker

[Docker](https://www.docker.com/) is a tool designed to make it easier to create, deploy, and run applications by using containers. Containers allow a developer to package up an application with all of the parts it needs, such as libraries and other dependencies, and ship it all out as one package. 

In this project other than that, docker is used due to the scalability that provides. Scalability is a key element in a microservice enviroment and we will see why in the next sections of this document.

#### Spring Netflix/Cloud

Spring Cloud Netflix provides Netflix OSS integrations for Spring Boot apps through autoconfiguration and binding to the Spring Environment and other Spring programming model idioms. With a few simple annotations you can quickly enable and configure the common patterns inside your application and build large distributed systems with battle-tested Netflix components. The patterns provided include Service Discovery (Eureka), Circuit Breaker (Hystrix), Intelligent Routing (Zuul) and Client Side Load Balancing (Ribbon).

source : [here](https://spring.io/projects/spring-cloud-netflix).

#### Java & Spring

These tow needs no indroduction I guess. If you want to know why Spring check [here](https://spring.io/why-spring).

#### Python & Flask

Flask is a lightweight WSGI web application framework. It is designed to make getting started quick and easy, with the ability to scale up to complex applications. It began as a simple wrapper around Werkzeug and Jinja and has become one of the most popular Python web application frameworks.

Some of the template API's are written based on [Python](https://www.python.org/)& [Flask](https://flask.palletsprojects.com/en/1.1.x/) framework. 

#### Elastic Stack
Historically ELK is a bundle of three open source software projects: Elasticsearch, Logstash and Kibana. All these products are maintained by the company Elastic. This bundle consists of:

Elasticsearch, a NoSQL database based on the Lucene search engine.
Logstash, a server-side data processing pipeline that accepts data from various simultaneously, transforms it, and exports the data to various targets.
Kibana, a visualization layer that works on top of Elasticsearch.
Elastic has recently included a family of log shippers called Beats and renamed the stack as Elastic Stack. The solution is flexible and is mostly used to centralize logging requirements.

<!-- Microservices Architecture Section -->
## Microservice Architecture 

This is an h1 heading.

![alt text][logo2]

[logo2]: ./img/Microservice-Ecosystem.jpg "Dockerized Microservices"

### Service-list & coresponding content

This is an h2 heading

#### Cloud Configuration Server
This is an h3 heading

#### Eurika Name Server
This is an h3 heading

#### Secrets Service

#### Graph API

#### Session Managment

#### Hello Service

#### Python Service

#### Hello Client

#### Gateway Proxy Service

#### Elastic Stack

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

![alt text][logo4]

[logo4]: ./img/elk.png "Elastic Stack"

In a few words:

* Filebeat collects data from the log files and sends it to Logststash.
* Logstash enhances the data and sends it to Elasticsearch.
* Elasticsearch stores and indexes the data.
* Kibana displays the data stored in Elasticsearch.

source : [here](https://cassiomolin.com/2019/06/30/log-aggregation-with-spring-boot-elastic-stack-and-docker/).

#### Apache as HTTPS proxy




<!-- Docker Architecture Section -->

## Docker Architecture 

This is an h1 heading

![alt text][logo3]

[logo3]: ./img/dockerrizing-microservices.jpg "Dockerized Microservices"

### Docker Containers & Spotofy Maven plugin.

### Docker Networks.

### Docker Volumes.

## Run The System

### Local Run

### Docker Run

## Future Additions

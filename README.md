# docker-microservice-enviroment

>This document's goal is to describe the high-end architecture of the presented microservice ecosystem. The ecosystem currentely supports services written in 2 languages: python & java, but there is the posibility to increase that number uppon request.
## Table of Contnents
- [Overview](#Overview)
  * [Technologies-used ](#technologies-used )
    + [Microservices](#microservices)
    + [Docker](#docker)
    + [Spring Netflix/Cloud](#spring-netflix/cloud)
    + [Java & Spring](#java-&-spring)
    + [Python & Flask](#python-&-flask)
    + [Elastic Stack](#elastic-stack)
- [Heading](#heading-1)
  * [Sub-heading](#sub-heading-1)
    + [Sub-sub-heading](#sub-sub-heading-1)
- [Heading](#heading-2)
  * [Sub-heading](#sub-heading-2)
    + [Sub-sub-heading](#sub-sub-heading-2)



## Overview

Microservices & Docker is tow of the biggest trends of our era used in software industry. This project/template aims to demostrate a base template of a microservice enviroment that can be used in a varriety of different use-cases. This template focuses on :  Microservices Architecture/Implementation & Microservices with Docker Engine. The core "body" of the ecosystem is implemented in java by using the Spring [Cloud](https://spring.io/projects/spring-cloud)/[ Netflix](https://spring.io/projects/spring-cloud-netflix) libraries. 

### Technologies-used 

In this section we will present the core technologies & concepts that the present ecosystem is depending on.


![alt text][logo]

[logo]: ./img/technplogies.jpg "Logo Title Text 2"

#### Microservices
Microservices - also known as the microservice architecture - is an architectural style that structures an application as a collection of services that are :

1. Highly maintainable and testable
2. Loosely coupled
3. Independently deployable
4. Organized around business capabilities
5. Owned by a small team
6. The microservice architecture enables the rapid, frequent and reliable delivery of large, complex applications. It also enables an organization to evolve its   technology stack.

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
## Heading

This is an h1 heading

### Sub-heading

This is an h2 heading

#### Sub-sub-heading

This is an h3 heading

## Heading

This is an h1 heading

### Sub-heading

This is an h2 heading

#### Sub-sub-heading

This is an h3 heading

## Service list & coresponding content
asdasd

## High End architecture discription
dsad

## Installation details
dsad

## Future additions 

FROM python:3.6-stretch
MAINTAINER GEORGIOS FIOTAKIS <GEORGIOS.FIOTAKIS@AKKA.EU>

ENV DEBUG true
# copy source code
COPY . /app


WORKDIR /app

# install requirements
RUN pip install -r requirements.txt

ENV CONFIGSERVER_ADDRESS=http://config-server:8888
ENV BRANCH=master
ENV PROFILE=docker
ENV APP_NAME=python-service 
ENV CONFIG_FAIL_FAST=True
# run the app server
CMD ["python3" , "config-client.py"]

FROM python:3.6-stretch

RUN apt-get update -y
RUN apt-get install -y python-pip python-dev

ENV CELERY_BROKER_URL pyamqp://abstract:admin@rabbit-server/
ENV CELERY_RESULT_BACKEND rpc://abstract:admin@rabbit-server/
ENV C_FORCE_ROOT true


# copy source code
COPY . /flask-celery
WORKDIR /flask-celery

# install requirements
RUN pip install -r requirements.txt


# run the worker
#ENTRYPOINT ['celery']
#CMD ['-A','tasks', 'worker','--loglevel=info']
ENTRYPOINT celery -A tasks worker --loglevel=info

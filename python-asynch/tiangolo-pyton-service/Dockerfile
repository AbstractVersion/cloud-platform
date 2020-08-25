FROM tiangolo/uwsgi-nginx-flask:python3.8

MAINTAINER GEORGIOS FIOTAKIS <GEORGIOS.FIOTAKIS@AKKA.EU>


ENV CELERY_BROKER_URL pyamqp://abstract:admin@rabbit-server/
ENV CELERY_RESULT_BACKEND rpc://abstract:admin@rabbit-server/
ENV C_FORCE_ROOT true

ENV HOST 0.0.0.0



COPY . /app
RUN chmod +x /app/prestart.sh

# install requirements
RUN pip install -r /app/requirements.txt

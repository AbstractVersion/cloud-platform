FROM python:3.6-stretch
MAINTAINER GEORGIOS FIOTAKIS <GEORGIOS.FIOTAKIS@AKKA.EU>


# ENV CELERY_BROKER_URL pyamqp://abstract:admin@rabbit-server/
# ENV CELERY_RESULT_BACKEND rpc://abstract:admin@rabbit-server/
ENV C_FORCE_ROOT true

ENV HOST 0.0.0.0
ENV DEBUG true
# copy source code
COPY . /app
RUN chmod +x /app/prestart.sh

WORKDIR /app

# install requirements
RUN pip install -r requirements.txt

# expose the app port
EXPOSE 5001

# run the app server
CMD ["./prestart.sh", "gunicorn", "-c", "gunicorn_config.py", "wsgi:app"]
# CMD ["./prestart.sh", "python3", "app.py"]
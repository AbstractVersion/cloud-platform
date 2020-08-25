import os
from celery import Celery

env=os.environ
CELERY_BROKER_URL=env.get('CELERY_BROKER_URL','pyamqp://abstract:admin@rabbit-server/'),
CELERY_RESULT_BACKEND=env.get('CELERY_RESULT_BACKEND','rpc://abstract:admin@rabbit-server/')


celery= Celery('tasks',
                broker=CELERY_BROKER_URL,
                backend=CELERY_RESULT_BACKEND)

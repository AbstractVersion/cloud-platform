import os
from celery import Celery

# env=os.environ
# CELERY_BROKER_URL=env.get('CELERY_BROKER_URL','pyamqp://abstract:admin@rabbit-server/' ),
# CELERY_RESULT_BACKEND=env.get('CELERY_RESULT_BACKEND','rpc://abstract:admin@rabbit-server/')
CELERY_BROKER_URL = ''
CELERY_RESULT_BACKEND = ''
celery = None

def set_up_celery(app_context):
    print (app_context)
    CELERY_BROKER_URL = 'pyamqp://' + app_context.config.get('spring')['rabbitmq']['username']+':'+app_context.config.get('spring')['rabbitmq']['password'] +'@'+ app_context.config.get('spring')['rabbitmq']['host'] + '/'
    CELERY_RESULT_BACKEND = 'rpc://'+ app_context.config.get('spring')['rabbitmq']['username']+':'+app_context.config.get('spring')['rabbitmq']['password'] +'@'+ app_context.config.get('spring')['rabbitmq']['host'] + '/'

    celery = Celery('tasks',
                    broker=CELERY_BROKER_URL,
                    backend=CELERY_RESULT_BACKEND)

import os
import time
from celery import Celery

env=os.environ
CELERY_BROKER_URL=env.get('CELERY_BROKER_URL','pyamqp://abstract:admin@rabbit-server/'),
CELERY_RESULT_BACKEND=env.get('CELERY_RESULT_BACKEND','rpc://abstract:admin@rabbit-server/')

celery= Celery('tasks',
                broker=CELERY_BROKER_URL,
                backend=CELERY_RESULT_BACKEND)


@celery.task(name='mytasks.add')
def add(x, y, json):
    print(json)
    time.sleep(15) # lets sleep for a while before doing the gigantic addition task!
    return x + y

@celery.task(name='mytasks.longtask', bind=True)
def process(self , json):
    self.update_state(state='RECEIVED')
    time.sleep(20) # lets sleep for a while before doing the gigantic addition task!
    self.update_state(state='STARTED')
    time.sleep(20) # lets sleep for a while before doing the gigantic addition task!
    self.update_state(state='SUCCESS')
    return "OK"

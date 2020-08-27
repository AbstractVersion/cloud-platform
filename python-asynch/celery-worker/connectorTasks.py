from celery import Celery
# Json Logger
from pythonjsonlogger import jsonlogger
import sleuth, b3
#General Imports
import time, datetime, sys, flask, json , socket, uuid, logging, os

app_name = 'celery-worker'

env=os.environ
CELERY_BROKER_URL=env.get('CELERY_BROKER_URL','pyamqp://abstract:admin@rabbit-server/'),
CELERY_RESULT_BACKEND=env.get('CELERY_RESULT_BACKEND','rpc://abstract:admin@rabbit-server/')

celery= Celery('tasks',
                broker=CELERY_BROKER_URL,
                backend=CELERY_RESULT_BACKEND)

class CustomJsonFormatter(jsonlogger.JsonFormatter):
    def add_fields(self, log_record, record, message_dict):
        super(CustomJsonFormatter, self).add_fields(log_record, record, message_dict)
        log_record['application_name'] = app_name
        log_record['trace'] = {
            "trace_id": b3.values()['X-B3-TraceId'],
            "span_id": b3.values()['X-B3-TraceId'],
            "exportable":"false"
        } 
        if not log_record.get('timestamp'):
            # this doesn't use record.created, so it is slightly off
            now = datetime.datetime.utcnow().strftime('%Y-%m-%dT%H:%M:%S.%fZ')
            log_record['timestamp'] = now
        if log_record.get('level'):
            log_record['level'] = log_record['level'].upper()
        else:
            log_record['level'] = record.levelname

logger = logging.getLogger("test-logger")
logger.setLevel(logging.DEBUG)
json_handler = logging.StreamHandler()
formatter = CustomJsonFormatter('(timestamp) (application_name) (level) (name) (message) (trace)')
json_handler.setFormatter(formatter)
logger.addHandler(json_handler)


@celery.task(name='mytasks.add')
def add(x, y, json):
    print(json)
    time.sleep(15) # lets sleep for a while before doing the gigantic addition task!
    return x + y

@celery.task(name='mytasks.longtask', bind=True)
def process(self , json):
    logger.info('Asynch Task recieved.')
    self.update_state(state='RECEIVED')
    time.sleep(20) # lets sleep for a while before doing the gigantic addition task!
    logger.info('Begin Excecution of Asynch Task.')
    self.update_state(state='STARTED')
    time.sleep(20) # lets sleep for a while before doing the gigantic addition task!
    self.update_state(state='SUCCESS')
    logger.info('Finished Excecution of Asynch Task.')
    return "OK"

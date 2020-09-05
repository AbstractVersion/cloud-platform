import py_eureka_client.eureka_client as eureka_client
#Flask Imports
from flask import Flask, url_for, jsonify, request, make_response
from flask_cors import CORS, cross_origin
#General Imports
import datetime, sys, flask, json , socket, uuid, logging, os
# Json Logger
from pythonjsonlogger import jsonlogger
import sleuth, b3
#Celery Imports
import celery.states as states
from worker import celery
from config.spring import ConfigClient
from config.ext.flask import FlaskConfig

os.environ.setdefault('FORKED_BY_MULTIPROCESSING', '1')

app = flask.Flask(__name__)
# CORS Headder 
CORS(app)
app_name = os.environ['APP_NAME']
#Configuration of Cloud-config server
FlaskConfig(app, ConfigClient(app_name=app_name, url="{address}/{branch}/{profile}-{app_name}.yaml"))

info = {'servicID': uuid.uuid1(),
     'serviceHost': socket.gethostname(),
     'title': 'Python API v.01'}

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


#Configure Eurika client
eureka_client.init(eureka_server="http://abstract:admin@discovery-service:8761/eureka",
		   instance_port=5001,
           app_name = app_name,
		   ha_strategy=eureka_client.HA_STRATEGY_STICK)

# A route to return all of the available entries in our catalog.
@app.route('/api/throw-exception', methods=['GET'])
def throw_exception():
    b3.start_span()
    logger.info('Trowing Test Exception')
    try:
        1 / 0
    except:  # noqa pylint: disable=bare-except
        logger.exception('You can\'t divide by zero')     
    b3.end_span()
    return "Done"

@app.route('/api/info', methods=['GET'])
def api_all():
    #https://github.com/davidcarboni/B3-Propagation
    # logger = logging.getLogger("werkzeug")
    # logger.setLevel(logging.DEBUG)
    b3.start_span()
    # logger.info("Yes Please")
    # logger.debug(b3.values()['X-B3-TraceId'], extra = {'props' : {'extra_property' : 'extra_value'}})
    # logger.info("Custom Logger message Python API", extra = buildTraceInfo(app_name,b3.values()['X-B3-TraceId']) )
    logger.info("Status check Request recieved.")
    # halloLog()
    b3.end_span()
    return "SUccess"


@app.route('/api/longTask', methods=['POST'])
def longTask():
    b3.start_span()
    logger.info("Recieved long-task request. Excecuting asynch.")
    task = celery.send_task('mytasks.longtask', args=[info, buildTraceInfo() ], kwargs={})
    b3.end_span()
    return jsonify({"task_id" : task.task_id, "URL" : url_for('check_task',task_id=task.task_id,_external=True)})


# Asynch Request status endpoint
@app.route('/api/status/<string:task_id>', methods=['GET'])
def check_task(task_id):
    res = celery.AsyncResult(task_id)
    if res.state==states.RECEIVED:
        return res.state
    elif res.state==states.STARTED:
        return res.state
    else:
        return str(res.result)


# Asynch Request status endpoint
@app.route('/api/config', methods=['GET'])
def retrieve_config():
    logger.info('Configuration retrieval request, served.')
    return jsonify(
            config=app.config
        )

def buildTraceInfo():
    return {'trace' : {
            "trace_id": b3.values()['X-B3-TraceId'],
            "span_id": b3.values()['X-B3-TraceId'],
            "exportable":"false"
            }
        }
if __name__ == "__main__":
    app.before_request(b3.start_span)
    app.after_request(b3.end_span)
    app.run(host='0.0.0.0', port=int(5001), use_reloader=False)

import os
import py_eureka_client.eureka_client as eureka_client
from flask import Flask, url_for, jsonify, request, make_response
from celery import Celery, states
import celery.states as states
from flask_cors import CORS, cross_origin
import uuid , json
import socket
import sleuth
import b3
# from elk_logger import logger_init
import  logging, sys, json_logging
from worker import celery
from json_logger_cm import CustomJSONLog


os.environ.setdefault('FORKED_BY_MULTIPROCESSING', '1')
os.environ["ENABLE_JSON_LOGGING"] = "1"

serviceId = uuid.uuid1()
serviceHost = socket.gethostname()


# https://github.com/thangbn/json-logging-python
# hello = flask.Flask(__name__)
app_name = "python-service"

# app = Flask(__name__)
# CORS(app)

# # init the logger as usual
# logger_init()
# logger = logging.getLogger("werkzeug")
# logger.setLevel(logging.DEBUG)
# logger.addHandler(logging.StreamHandler(sys.stdout))

# You would normally import logger_init and setup the logger in your main module - e.g.
# main.py
json_logging.ENABLE_JSON_LOGGING = True
json_logging.init_non_web(custom_formatter=CustomJSONLog)

logger = logging.getLogger(__name__)
logger.setLevel(logging.DEBUG)
logger.addHandler(logging.StreamHandler(sys.stdout))

# Create some test data for our catalog in the form of a list of dictionaries.
info = {'servicID': serviceId,
     'serviceHost': serviceHost,
     'title': 'Python API v.01'}

app = Flask(__name__)
CORS(app)


#Configure Eurika client
eureka_client.init(eureka_server="http://abstract:admin@discovery-service:8761/eureka",
		   instance_port=5001,
           app_name=app_name,
		   ha_strategy=eureka_client.HA_STRATEGY_STICK)


@app.route('/', methods=['GET'])
def home():
    return {"endpoint-description" : "infinite-dmu-replicator", "clone-procedure" : "asynch", "version" : "1.0"}

# A route to return all of the available entries in our catalog.
@app.route('/api/info', methods=['GET'])
def api_all():
    #https://github.com/davidcarboni/B3-Propagation
    # logger = logging.getLogger("werkzeug")
    # logger.setLevel(logging.DEBUG)
    b3.start_span()
    logger.info('Starting')
    try:
        1 / 0
    except:  # noqa pylint: disable=bare-except
        logger.exception('You can\'t divide by zero')     
    # logger.debug(b3.values()['X-B3-TraceId'], extra = {'props' : {'extra_property' : 'extra_value'}})
    # logger.info("Custom Logger message Python API", extra = {'trace': buildTraceInfo()} )
    b3.end_span()
    return jsonify(info)

@app.route('/api/add/<int:param1>/<int:param2>')
def add(param1,param2):
    json = {"hi": "yes"}
    task = celery.send_task('mytasks.add', args=[param1, param2, json], kwargs={})
    return jsonify({"task_id" : task.id, "URL" : url_for('check_task',id=task.id,_external=True)})

@app.route('/api/longTask', methods=['POST'])
def longTask():
    b3.start_span()
    logger.info("Recieved long-task request. Excecuting asynch.")
    task = celery.send_task('mytasks.longtask', args=[request.json], kwargs={})
    b3.end_span()
    return jsonify({"task_id" : task.task_id, "URL" : url_for('check_task',id=task.task_id,_external=True)})


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


# def buildTraceInfo():
#     return {
#             "trace_id":b3.values()['X-B3-TraceId'],
#             "span_id":b3.values()['X-B3-TraceId'],
#             "exportable":"false"
#         }
def halloLog():
    logger.info('Connector is up & running.')

if __name__ == '__main__':
    app = create_app()
    app.before_request(b3.start_span)
    app.after_request(b3.end_span)
    app.run(host='0.0.0.0')

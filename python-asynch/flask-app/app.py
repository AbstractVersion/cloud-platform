import os
import py_eureka_client.eureka_client as eureka_client
from flask import Flask
from flask import  url_for, jsonify, request, make_response
from worker import celery
from celery.result import AsyncResult
import celery.states as states
import uuid , json
import socket
import sleuth
import b3
from elk_logger import logger_init
import  logging, sys, json_logging
from worker import celery


os.environ.setdefault('FORKED_BY_MULTIPROCESSING', '1')


serviceId = uuid.uuid1()
serviceHost = socket.gethostname()

# # init the logger as usual
logger_init()
logger = logging.getLogger("werkzeug")
logger.setLevel(logging.DEBUG)
logger.addHandler(logging.StreamHandler(sys.stdout))

env=os.environ
app_name = "python-service"

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

@app.route('/service-status')
def status():
    return jsonify(info)

@app.route('/add/<int:param1>/<int:param2>')
def add(param1,param2):
    json = {"hi": "yes"}
    task = celery.send_task('mytasks.add', args=[param1, param2, json], kwargs={})
    return "<a href='{url}'>check status of {id} </a>".format(id=task.id,
                url=url_for('check_task',id=task.id,_external=True))

@app.route('/longTask', methods=['POST'])
def longTask():
    b3.start_span()
    json = request.json
    logger.info("Recieved project : " + json.dumps(jsonRequest))
    task = celery.send_task('mytasks.longtask', args=[json], kwargs={})
    b3.end_span()
    return "<a href='{url}'>check status of {id} </a>".format(id=task.id,
                url=url_for('check_task',id=task.id,_external=True))

@app.route('/check/<string:id>')
def check_task(id):
    res = celery.AsyncResult(id)
    if res.state==states.RECEIVED:
        return res.state
    elif res.state==states.STARTED:
        return res.state
    else:
        return str(res.result)
if __name__ == '__main__':
    app.before_request(b3.start_span)
    app.after_request(b3.end_span)
    app.run(debug=env.get('DEBUG',True),
            port=int(env.get('PORT',5000)),
            host=env.get('HOST','0.0.0.0')
    )

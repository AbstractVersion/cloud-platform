import flask
import py_eureka_client.eureka_client as eureka_client
from flask import request, jsonify
import uuid 
import socket
import sleuth
import b3
import logging
from pythonjsonlogger import jsonlogger
import json

serviceId = uuid.uuid1()
serviceHost = socket.gethostname()

app = flask.Flask(__name__)

# https://github.com/thangbn/json-logging-python
# app = flask.Flask(__name__)
app_name = "py-app"


logger = logging.getLogger("werkzeug")

def json_translate(obj):
    if isinstance(obj, MyClass):
        return {"special": obj.special}

formatter = jsonlogger.JsonFormatter(json_default=json_translate,
                                     json_encoder=json.JSONEncoder)
logHandler = logging.StreamHandler()
logHandler.setFormatter(formatter)
logger.addHandler(logHandler)

app.config["DEBUG"] = True
eureka_client.init(eureka_server="http://discovery-service:8761/eureka",
		   instance_port=5001,
           app_name=app_name,
		   ha_strategy=eureka_client.HA_STRATEGY_STICK)

# Create some test data for our catalog in the form of a list of dictionaries.
info = {'servicID': serviceId,
     'serviceHost': serviceHost,
     'title': 'Python API v.01'}


@app.route('/', methods=['GET'])
def home():
    
    
    return '''<h1>Distant Reading Archive</h1>
<p>A prototype API for distant reading of science fiction novels.</p>'''


# A route to return all of the available entries in our catalog.
@app.route('/api/info', methods=['GET'])
def api_all():
    #https://github.com/davidcarboni/B3-Propagation
    # print(b3.values())
    # logger = logging.getLogger("werkzeug")
    # logger.setLevel(logging.DEBUG)
    b3.start_span()
    traceInfo = {
        "application_name": app_name,
        "trace":
        {
            "trace_id":b3.values()['X-B3-TraceId'],
            "span_id": b3.values()['X-B3-TraceId'],
            "exportable":"false"
        }
    }
    # logger.debug(b3.values()['X-B3-TraceId'], extra = {'props' : {'extra_property' : 'extra_value'}})
    # logger.info("Requested python APi information")
    # logger.info("Hey")
    logger.info("classic message", extra=traceInfo)
    b3.end_span()
    return jsonify(info)


if __name__ == '__main__':
    # with app.app_context():
    # Go!
    # logger.info("Starting API")
    app.before_request(b3.start_span)
    app.after_request(b3.end_span)
    app.run(
        host="0.0.0.0",
        debug=True,
        threaded=True,
        port=5001,
        use_reloader=False
    )
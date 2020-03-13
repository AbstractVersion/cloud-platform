import flask
import py_eureka_client.eureka_client as eureka_client
from flask import request, jsonify
import uuid 
import socket
import logging
import sleuth
import b3

serviceId = uuid.uuid1()
serviceHost = socket.gethostname()

app = flask.Flask(__name__)

app.config["DEBUG"] = True
eureka_client.init(eureka_server="http://discovery-service:8761/eureka",
		   instance_port=5001,
                   app_name="py-app",
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
    logger = logging.getLogger("python-app")
    logger.setLevel(logging.INFO)
    b3.start_span()
    logger.info("Requested python APi information")
    logger.info("Hey")
    b3.end_span()
    return jsonify(info)


if __name__ == '__main__':
    # with app.app_context():
    # Go!
    logger.info("Starting API")
    app.before_request(b3.start_span)
    app.after_request(b3.end_span)
    app.run(
        host="0.0.0.0",
        debug=True,
        threaded=True,
        port=5001
    )
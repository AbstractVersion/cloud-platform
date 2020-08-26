import os
import py_eureka_client.eureka_client as eureka_client
from flask import Flask, url_for, jsonify, request, make_response
import datetime, sys, flask, json
import logging
from pythonjsonlogger import jsonlogger
import sleuth, b3

app = flask.Flask(__name__)

def setup_logging(log_level):
    logger = logging.getLogger(__name__)
    logger.setLevel(log_level)
    json_handler = logging.StreamHandler()
    formatter = jsonlogger.JsonFormatter(
        fmt='%(asctime)s %(levelname)s %(name)s %(message)s'
    )
    json_handler.setFormatter(formatter)
    logger.addHandler(json_handler)

setup_logging()

#Configure Eurika client
eureka_client.init(eureka_server="http://abstract:admin@discovery-service:8761/eureka",
		   instance_port=5001,
           app_name="test-application",
		   ha_strategy=eureka_client.HA_STRATEGY_STICK)

@app.route('/')
def home():
    logger.info("test log statement")
    logger.info("test log statement with extra props", extra={'props': {"extra_property": 'extra_value'}})
    return "Hello world : " + str(datetime.datetime.now())

@app.route('/api/info', methods=['GET'])
def api_all():
    #https://github.com/davidcarboni/B3-Propagation
    # logger = logging.getLogger("werkzeug")
    # logger.setLevel(logging.DEBUG)
    # b3.start_span()
    logger.info("Yes Please")
    # logger.debug(b3.values()['X-B3-TraceId'], extra = {'props' : {'extra_property' : 'extra_value'}})
    # logger.info("Custom Logger message Python API", extra = buildTraceInfo(app_name,b3.values()['X-B3-TraceId']) )
    logger.info("test log statement", extra = {'trace' : {
            "trace_id":b3.values()['X-B3-TraceId'],
            "span_id":b3.values()['X-B3-TraceId'],
            "exportable":"false"
        }})
    # halloLog()
    # b3.end_span()
    return "SUccess"

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=int(5001), use_reloader=False)




# @app.route('/', methods=['GET'])
# def home():
#     return {"endpoint-description" : "infinite-dmu-replicator", "clone-procedure" : "asynch", "version" : "1.0"}

# # A route to return all of the available entries in our catalog.
# @app.route('/api/info', methods=['GET'])
# def api_all():
#     #https://github.com/davidcarboni/B3-Propagation
#     # logger = logging.getLogger("werkzeug")
#     # logger.setLevel(logging.DEBUG)
#     b3.start_span()
#     logger.info("Yes Please")
#     # logger.debug(b3.values()['X-B3-TraceId'], extra = {'props' : {'extra_property' : 'extra_value'}})
#     # logger.info("Custom Logger message Python API", extra = buildTraceInfo(app_name,b3.values()['X-B3-TraceId']) )
#     logger.info("test log statement", extra = {'trace' : {
#             "trace_id":b3.values()['X-B3-TraceId'],
#             "span_id":b3.values()['X-B3-TraceId'],
#             "exportable":"false"
#         }})
#     halloLog()
#     b3.end_span()
#     return "SUccess"




# # Asynch Request status endpoint
# @app.route('/api/status/<string:task_id>', methods=['GET'])
# def taskstatus(task_id):
#     res = celery.AsyncResult(task_id)
#     if res.result is None :
#         return jsonify({'status' : res.state}), 202
#     else:
#         return jsonify(res.result), 200


# def halloLog():
#     logger.info('Connector is up & running.')

# if __name__ == '__main__':
#     # app = create_app()
#     app.before_request(b3.start_span)
#     app.after_request(b3.end_span)
#     app.run(host='0.0.0.0')

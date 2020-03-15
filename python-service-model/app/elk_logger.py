import logging
from pythonjsonlogger import jsonlogger
import json



# https://github.com/madzak/python-json-logger
logger = logging.getLogger("werkzeug")

def json_translate(obj):
    if isinstance(obj, MyClass):
        return {"special": obj.special}

formatter = jsonlogger.JsonFormatter(json_default=json_translate,
                                     json_encoder=json.JSONEncoder)
logHandler = logging.StreamHandler()
logHandler.setFormatter(formatter)
logger.addHandler(logHandler)

def elk_log_info(message, b3, app_name):
    logger.info(message, extra=buildTraceInfo("INFO",app_name,b3.values()['X-B3-TraceId']))


def elk_log_debug(message, b3, app_name):
    logger.info(message, extra=buildTraceInfo("DEBUG",app_name,b3.values()['X-B3-TraceId']))



def elk_log_error(message, b3, app_name):
    logger.info(message, extra=buildTraceInfo("ERROR",app_name,b3.values()['X-B3-TraceId']))

def buildTraceInfo(level, app_name, trace_id):
    return {
            "level": level,
            "application_name": app_name,
            "trace":
            {
                "trace_id":trace_id,
                "span_id": trace_id,
                "exportable":"false"
            }
        }
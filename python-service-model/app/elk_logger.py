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

def elk_log(message, b3):
    traceInfo = {
            "level": logging.getLogger().getEffectiveLevel(),
            "application_name": app_name,
            "trace":
            {
                "trace_id":b3.values()['X-B3-TraceId'],
                "span_id": b3.values()['X-B3-TraceId'],
                "exportable":"false"
            }
        }
    logger.info("Python Api logging", extra=traceInfo)


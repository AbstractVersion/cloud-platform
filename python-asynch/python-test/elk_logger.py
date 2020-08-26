
from pythonjsonlogger import jsonlogger
import sleuth, b3
import logging
# https://github.com/madzak/python-json-logger

app_name = 'default-application'

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
    def ste_application_name(appName):
        app_name = appName
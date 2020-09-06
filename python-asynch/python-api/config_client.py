# https://pypi.org/project/spring-config-client/
# https://pypi.org/project/config-client/
from spring_config import ClientConfigurationBuilder
from spring_config.client import SpringConfigClient
import os,  json
# from custom_json_logger import setUpLogger
import datetime
from celery import Celery


os.environ["CONFIGSERVER_ADDRESS"] = 'http://config-server:8888/'
os.environ["PROFILE"] = "docker"
os.environ["APP_NAME"] = "python-service"
os.environ["CONFIG_FAIL_FAST"] = "True"
os.environ["CELERY_TASK_LIST"] = "tasks"

config_client = None

def setUpConfigClient():
    # Spring cloud configuration
    config = (
        ClientConfigurationBuilder()
        .app_name(os.environ["APP_NAME"])
        .address(os.environ["CONFIGSERVER_ADDRESS"])
        .profile(os.environ["PROFILE"])
        .build()
    )
    # Set up config client 
    return SpringConfigClient(config)

def getGlobalConfigurations(config_client):
    return json.dumps(config_client.get_config())

def getEurikaRegistrationURI(config_client):
    json_buffer = json.loads(getGlobalConfigurations(config_client))
    return json_buffer['eureka']['client']['serviceUrl']['defaultZone']

def getEurikaRegistrationInformation(config_client):
    json_buffer = json.loads(getGlobalConfigurations(config_client))
    return json_buffer['eureka']

def getRabbitMQRegistrationInformation(config_client):
    json_buffer = json.loads(getGlobalConfigurations(config_client))
    return json_buffer['spring']['rabbitmq']

def set_up_celery(config_client):
    rabbit_mq_configs = getRabbitMQRegistrationInformation(config_client)
    CELERY_BROKER_URL = 'pyamqp://' + rabbit_mq_configs['username']+':'+rabbit_mq_configs['password'] +'@'+ rabbit_mq_configs['host'] + '/'
    CELERY_RESULT_BACKEND = 'rpc://'+ rabbit_mq_configs['username']+':'+rabbit_mq_configs['password'] +'@'+ rabbit_mq_configs['host'] + '/'
    return Celery(os.environ["CELERY_TASK_LIST"],
                    broker=CELERY_BROKER_URL,
                    backend=CELERY_RESULT_BACKEND)


# config_client = setUpConfigClient()
# print("Config server connection : success.")

# celery = set_up_celery()
# print("Celery configuration : success.")



# # print(j)
# print (getEurikaRegistrationURI(config_client))
# print (getRabbitMQRegistrationInformation(config_client))
# print (getEurikaRegistrationInformation(config_client))
# set_up_celery(config_client)
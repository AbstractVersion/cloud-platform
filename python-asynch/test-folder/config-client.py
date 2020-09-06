from config.spring import ConfigClient
import os

config_client = ConfigClient(app_name=os.environ['APP_NAME'])
config_client.get_config(timeout=5.0, headers={'Accept': 'application/json'})

# option 1: dict like with direct access
config_client.config['spring']['cloud']['consul']['host']

# option 2: dict like using get
config_client.config.get('spring').get('cloud').get('consul').get('port')

# option 3: using get_attribute method
print(config_client.get_attribute('spring.main.rabbitmq.host'))

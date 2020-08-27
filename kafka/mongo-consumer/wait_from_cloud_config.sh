#!/bin/sh
# wait-for-postgres.sh



bash -c 'while [[ "$(curl -s -o /dev/null -w ''%{http_code}'' producer-service:8080/actuator)" != "200" ]]; do sleep 5; done'


>&2 echo "consumer-service server is up & running - executing command"

exec "$@"

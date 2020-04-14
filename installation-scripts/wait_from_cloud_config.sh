#!/bin/sh
# wait-for-postgres.sh

set -e

cmd="$@"

bash -c 'while [[ "$(curl -s -o /dev/null -w ''%{http_code}'' config-server:8888/actuator)" != "200" ]]; do sleep 5; done'


>&2 echo "Cloud Config server is up & running - executing command"
exec $cmd
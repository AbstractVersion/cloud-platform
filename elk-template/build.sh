
echo "Configuration of your terminal."
read -p "Build on production development(local) | development (dev) | prodiction(swarm)? (local/dev/swarm) " RESP
if [ "$RESP" = "swarm" ]; then
    echo "Building on swarm (production)."
    source .swarm.env
elif [ "$RESP" = "dev"  ]; then
    echo "Building on swarm development (development)."
    source .dev.env
else
    echo "Building on development."
    source ~/.bash_profile
    source .local.env
fi

docker image pull docker.elastic.co/elasticsearch/elasticsearch:7.13.2
docker image pull docker.elastic.co/logstash/logstash:7.13.2
docker image pull docker.elastic.co/kibana/kibana:7.13.2
docker image pull docker.elastic.co/beats/filebeat:7.13.2


echo 'building filebeat'
cd filebeat
docker image build -t ${REGISTRY_URL}/filebeat:7.13.2 .
docker image push ${REGISTRY_URL}/filebeat:7.13.2

echo 'building elastic-search'
docker image tag docker.elastic.co/elasticsearch/elasticsearch:7.13.2 ${REGISTRY_URL}/elasticsearch:7.13.2
docker image push ${REGISTRY_URL}/elasticsearch:7.13.2

echo 'building kibana'
docker image tag docker.elastic.co/kibana/kibana:7.13.2 ${REGISTRY_URL}/kibana:7.13.2
docker image push ${REGISTRY_URL}/kibana:7.13.2

echo 'building logstash'
docker image tag docker.elastic.co/logstash/logstash:7.13.2 ${REGISTRY_URL}/logstash:7.13.2
docker image push ${REGISTRY_URL}/logstash:7.13.2

# VM Config memory
# https://stackoverflow.com/questions/51445846/elasticsearch-max-virtual-memory-areas-vm-max-map-count-65530-is-too-low-inc



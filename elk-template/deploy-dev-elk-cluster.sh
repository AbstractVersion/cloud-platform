 # env $(cat .dev.env | grep ^[A-Z] | xargs) 
 docker stack deploy --compose-file docker-compose-elk-cluster.yml elastic-stack-cluster
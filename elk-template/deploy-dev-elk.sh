 #env $(cat .dev.env | grep ^[A-Z] | xargs) 
 docker stack deploy --compose-file docker-compose.swarm.yml elastic-stack
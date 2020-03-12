# Docker Compose pre run details.

## Filebeat

For this container the filebeat.docker.yml must be owned by the root user & the permissions as well must be only on the root.

chmod 600 ./filebeat.docker.yml
chown -R root:root ./filebeat.docker.yml
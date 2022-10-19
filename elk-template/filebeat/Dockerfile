FROM docker.elastic.co/beats/filebeat:7.13.2

COPY ./filebeat.docker.yml /usr/share/filebeat/filebeat.yml
# COPY --chown=root:filebeat filebeat.yml /usr/share/filebeat/filebeat.yml
USER root
RUN chmod 600 /usr/share/filebeat/filebeat.yml

# ENTRYPOINT ["$@"]
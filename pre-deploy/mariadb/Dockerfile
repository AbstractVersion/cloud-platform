FROM mariadb:latest

ENV MARIADB_USER=root
ENV MARIADB_PASSWORD=root
ENV MARIADB_DATABASE=sessionDB

COPY *.sh /docker-entrypoint-initdb.d/
COPY *.sql /docker-entrypoint-initdb.d/

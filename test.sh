#!/bin/sh
# This is a comment!

for NODE in $(docker node ls --filter role=manager --format '{{.Hostname}} ')
do 
    echo  "Adding as client :\t${NODE} - $(docker node inspect --format '{{.Status.Addr}}' "${NODE}")"; 
    sudo -u root echo  "/mnt/sharedfolder $(docker node inspect --format '{{.Status.Addr}}' "${NODE}")(rw,sync,no_subtree_check)" #>> /etc/exports

    echo "Allowing client through debian ip-tables : \t" $(docker node inspect --format "{{.Status.Addr}}" ${NODE})
    echo 'sudo -u iptables -A INPUT -s '$(docker node inspect --format "{{.Status.Addr}}" ${NODE})' -j ACCEPT'
    echo '\n'
done
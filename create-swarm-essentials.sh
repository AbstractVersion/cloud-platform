#!/bin/sh
# This is a comment!
echo ----------------------------------------------- Active Hosts of the Swarm Cluster -------------------------------------------


echo ---- Arctive Swarm Nodes----

docker node ls

echo "\n"

echo ----------------------------------------------- Managers of the Swarm Cluster -----------------------------------------------

docker node ls --filter role=manager

echo "\n"

echo ----------------------------------------------- Workers of the Swarm Cluster ------------------------------------------------
docker node ls --filter role=worker




# Docker Swarm Overlay Networks
echo Current Active networks :

docker network ls

read -p "Create Shared volumes ? (y/n) " RESP
if [ "$RESP" = "y" ]; then
    echo Creating Swarm Networks

        docker network create -d overlay micro-nework-frontend
        docker network create -d overlay micro-nework-backend
        docker network create -d overlay elastic-stack-network

        echo Current Active networks :

        docker network ls
else
    echo "Ok then proceeding with the initialization..."
fi






#------------------------------------ Initializeing NFS ---------------------------------------------
#https://vitux.com/install-nfs-server-and-client-on-ubuntu/


read -p "Do you want to install NFS server here ? (y/n) " RESP
if [ "$RESP" = "y" ]; then
    echo 'Installing NFS Server for File sharing, you will need a sudoer'
    #Update apt & install nfs kernel
    sudo apt-get update
    sudo apt install nfs-kernel-server

    # Create shared directory, you can change this value to point to the directory of your preferences, just bear in mind that this directory will
    # be shared accross all manager nodes.
    sudo mkdir -p /mnt/sharedfolder

    # Assigne the filesystem permissions so as the directory to be accessible by the NFS.
    sudo chown nobody:nogroup /mnt/sharedfolder
    sudo chmod 777 /mnt/sharedfolder
    
    sudo cat /etc/exports

    
    echo 'Adding clients to NFS, you will need root for that'
    sudo -u root rm -rf ./exports
    touch ./exports
    for NODE in $(docker node ls --filter role=manager --format '{{.Hostname}}')
    do 
        echo  "Adding as client :\t${NODE} - $(docker node inspect --format '{{.Status.Addr}}' "${NODE}")"
        q=$(docker node inspect --format '{{.Status.Addr}}' ${NODE})
        echo  "/mnt/sharedfolder $q(rw,sync,no_subtree_check)" >> ./exports
        # echo '"/mnt/sharedfolder '$q'(rw,sync,no_subtree_check)" >> /etc/exports'
        
        # temp = "$(docker node inspect --format '{{.Status.Addr}}' "${NODE}"
        # echo $temp
        # sudo echo  "/mnt/sharedfolder $temp(rw,sync,no_subtree_check)"$'\r' >> /etc/exports

        echo "Allowing client through debian ip-tables : \t" $(docker node inspect --format "{{.Status.Addr}}" ${NODE})
        sudo -u root iptables -A INPUT -s $q -j ACCEPT
        echo '\n'
    done
    sudo -u root cp -fr ./exports /etc/exports
    # After making all the above configurations in the host system, 
    # now is the time to export the shared directory through the following command as sudo:
    sudo -u root exportfs -a

    # Finally, in order to make all the configurations take effect, 
    # restart the NFS Kernel server as follows:

    sudo -u root systemctl restart nfs-kernel-server
else
    read -p "Do you want to configure it as a client ? (y/n) " RESP
    if [ "$RESP" = "y" ]; then

        sudo -u root apt-get update && sudo -u root apt-get install nfs-common
        sudo -u root mkdir -p /mnt/sharedfolder
        echo 'please provide the NFS server ip :'
        read input
        sudo -u root mount $input:/mnt/sharedfolder
    else
    echo "Ok then proceeding with the initialization..."
    fi
fi


read -p "Create Shared volumes ? (y/n) " RESP
if [ "$RESP" = "y" ]; then
    sudo -u root mkdir -p /mnt/sharedfolder/volumes/data/elsasticsearch
    # sudo -u root mkdir -p /mnt/sharedfolder/volumes/data/mariadb      
    ip="$(ifconfig | grep -A 1 'eth0' | tail -1 | cut -d ':' -f 2 | cut -d ' ' -f 1)"

    docker volume create --driver local \
      --opt type=nfs \
      --opt o=nfsvers=4,addr=$ip,rw \
      --opt device=:/mnt/sharedfolder/volumes/data/elsasticsearch \
      elsasticsearch-volume
else
    echo "Ok then proceeding with the initialization..."
fi


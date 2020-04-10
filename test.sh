#!/bin/sh
# This is a comment!

# for NODE in $(docker node ls --filter role=manager --format '{{.Hostname}} ')
# do 
#     echo  "Adding as client :\t${NODE} - $(docker node inspect --format '{{.Status.Addr}}' "${NODE}")"; 
#     sudo -u root echo  "/mnt/sharedfolder $(docker node inspect --format '{{.Status.Addr}}' "${NODE}")(rw,sync,no_subtree_check)" #>> /etc/exports

#     echo "Allowing client through debian ip-tables : \t" $(docker node inspect --format "{{.Status.Addr}}" ${NODE})
#     echo 'sudo -u iptables -A INPUT -s '$(docker node inspect --format "{{.Status.Addr}}" ${NODE})' -j ACCEPT'
#     echo '\n'
# done

read -p "Are you alright? (y/n) " RESP
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
    touch ./exports
    for NODE in $(docker node ls --filter role=manager --format '{{.Hostname}}')
    do 
        echo  "Adding as client :\t${NODE} - $(docker node inspect --format '{{.Status.Addr}}' "${NODE}")"
        q=$(docker node inspect --format '{{.Status.Addr}}' ${NODE})
        echo  "/mnt/sharedfolder $q(rw,sync,no_subtree_check)"$'\r' >> ./exports
        # echo '"/mnt/sharedfolder '$q'(rw,sync,no_subtree_check)" >> /etc/exports'
        
        # temp = "$(docker node inspect --format '{{.Status.Addr}}' "${NODE}"
        # echo $temp
        # sudo echo  "/mnt/sharedfolder $temp(rw,sync,no_subtree_check)"$'\r' >> /etc/exports

        echo "Allowing client through debian ip-tables : \t" $(docker node inspect --format "{{.Status.Addr}}" ${NODE})
        sudo -u root iptables -A INPUT -s $q -j ACCEPT
        echo '\n'
    done
    sudo -u root copy ./exports /etc/exports
    # After making all the above configurations in the host system, 
    # now is the time to export the shared directory through the following command as sudo:
    sudo -u root exportfs -a

    # Finally, in order to make all the configurations take effect, 
    # restart the NFS Kernel server as follows:

    sudo -u root systemctl restart nfs-kernel-server
else
     #Initializing NFS client
    echo "Initialize NFS Client ? [Y,n]"
    read input
    if [[ $input == "Y" || $input == "y" ]]; then
        sudo -u root apt-get update && sudo -u root apt-get install nfs-common
        sudo -u root mkdir -p /mnt/sharedfolder
        echo 'please provide the NFS server ip :'
        read input
        sudo -u root mount $input:/mnt/sharedfolder 
    else
        echo "Proceeding with the initialization"
    fi
    echo "Proceeding with the initialization"
fi
#!/bin/sh
# create private registry for swarm cluster image distribution
sudo apt install -y gnupg2 pass apache2-utils httpie
sudo apt install tree

read -p "Do you want initialize this node as registry server ?" RESP
if [ "$RESP" = "y" ]; then
    # Check docker & compose version
    docker version
    docker-compose version

    # - Create Project Directories
    # mkdir -p docker-registry/{nginx,auth}
    # cd docker-registry/
    # mkdir -p nginx/{conf.d/,ssl}
    # tree
    # cd registry/

    read -p "Do you want to create a new certificate ? (y/n) " RESP
    if [ "$RESP" = "y" ]; then
        echo Generate Certificate
        #Generate certficates
        openssl req -newkey rsa:2048 -new -nodes -x509 -days 3650 -keyout privkey.pem -out fullchain.pem
        mv privkey.pem docker-registry/nginx/ssl/privkey.pem
        mv fullchain.pem docker-registry/nginx/ssl/fullchain.pem
        tree
    else
        echo "To provide your own certificates please paste them in pem format to :"
        echo "docker-registry/nginx/ssl/fullchain.pem"
        echo "docker-registry/nginx/ssl/privkey.pem"
    fi

    read -p "Do you want to create a new user ? (y/n) " RESP
    if [ "$RESP" = "y" ]; then
        echo Creating new uesr
        read -p "please provide the user name: " username

         # Authentication user for proxy pass
        cd docker-registry/auth/
        htpasswd -Bc registry.passwd $username   
        tree
        cat registry.passwd
        cd ../..
    else
        echo "To provide your own credentials please paste them in htpasswd format to :"
        echo "docker-registry/auth/registry.passwd"
    fi

    read -p "Do you want to add as trusted the registry certificates by docker && OS? (y/n) " RESP
    if [ "$RESP" = "y" ]; then
        echo "trusting certificates, please select the rootCA from extra folder as trusted source"
        # Generate crt from pem to add to trusted domains of OS & docker
        openssl x509 -in docker-registry/nginx/ssl/fullchain.pem -inform PEM -out docker-registry/nginx/ssl/private-registry-cert.crt

        # Now create a new directory for docker certificate and copy the Root CA certificate into it.
        sudo mkdir -p /etc/docker/certs.d/private.registry.io/
        sudo cp docker-registry/nginx/ssl/private-registry-cert.crt /etc/docker/certs.d/private.registry.io/

        # And then create a new directory '/usr/share/ca-certificate/extra' and copy the Root CA certificate into it.
        sudo mkdir -p /usr/share/ca-certificates/extra/
        sudo cp docker-registry/nginx/ssl/private-registry-cert.crt /usr/share/ca-certificates/extra/

        # Update certificates & restart docker
        sudo dpkg-reconfigure ca-certificates
        sudo systemctl restart docker
    else
        echo "To provide your own credentials please paste them in htpasswd format to :"
        echo "docker-registry/auth/registry.passwd"
    fi

    read -p "Do you want to add ? (y/n) " RESP
    if [ "$RESP" = "y" ]; then
        # Edit hosts file
        sudo -u root echo '127.0.0.1    private.registry.io' >> /etc/hosts
    else
        echo "To provide your own credentials please paste them in htpasswd format to :"
        echo "docker-registry/auth/registry.passwd"
    fi
    # Create docker compose
    docker-compose up -d
    docker-compose ps
    # http -a abstract https://private.registry.io/v2/_catalog

    
    # Push image : 
    # docker pull ubuntu:16.04
    # docker image tag ubuntu:16.04 private.registry.io/ubuntu16
    # docker login https://private.registry.io/v2/
    # http -a abstract https://private.registry.io/v2/_catalog
    # docker push private.registry.io/ubuntu16

else
    echo "configuring repository client"
    read -p "Enter registry IP address: "  registry_ip

    sudo su
    echo $registry_ip'    private.registry.io' >> /etc/hosts
    exit
    openssl x509 -in docker-registry/nginx/ssl/fullchain.pem -inform PEM -out docker-registry/nginx/ssl/private-registry-cert.crt

    # Now create a new directory for docker certificate and copy the Root CA certificate into it.
    sudo mkdir -p /etc/docker/certs.d/private.registry.io/
    sudo cp docker-registry/nginx/ssl/private-registry-cert.crt /etc/docker/certs.d/private.registry.io/

    # And then create a new directory '/usr/share/ca-certificate/extra' and copy the Root CA certificate into it.
    sudo mkdir -p /usr/share/ca-certificates/extra/
    sudo cp docker-registry/nginx/ssl/private-registry-cert.crt /usr/share/ca-certificates/extra/

    # Update certificates & restart docker
    sudo dpkg-reconfigure ca-certificates
    sudo systemctl restart docker
fi

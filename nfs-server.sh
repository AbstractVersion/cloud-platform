# In order to mount NFS drives we need to install the following:
sudo -u root apt install nfs-common -y

# Create NFS Server:
# We have 2 options, we can either install a NFS Server (you can follow this post to setup a NFS Server )  
# or we can setup a NFS Server using Docker (explained below):

# Prepare the NFS Home Directory:
mkdir /nfsdata


docker run --rm -itd --name nfs \
  --privileged \
  -v /nfsdata:/nfs.1 \
  -e SHARED_DIRECTORY=/nfs.1 \
  -p 192.168.2.4:2049:2049 \
  itsthenetwork/nfs-server-alpine:latest

#   Install Netshare Docker Volume Driver
# Install Netshare which will provide the NFS Docker Volume Driver:
$ wget https://github.com/ContainX/docker-volume-netshare/releases/download/v0.36/docker-volume-netshare_0.36_amd64.deb
$ dpkg -i docker-volume-netshare_0.36_amd64.deb
$ service docker-volume-netshare start
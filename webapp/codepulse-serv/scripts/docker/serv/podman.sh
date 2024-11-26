#!/usr/bin/env bash

# 构建后端服务的docker镜像, FROM openjdk:17-oracle
podman build -t codepulse_serv:v1 .

# 容器存在，则删除
container_name="codepulse-serv"
if [ "$(podman ps -qa -f name=$container_name)" ]; then
    echo ":: Found container - $container_name"
    if [ "$(podman ps -q -f name=$container_name)" ]; then
        echo ":: Stopping running container - $container_name"
        podman stop $container_name;
    fi
    echo ":: Removing stopped container - $container_name"
    podman rm $container_name;
else
    echo ":: container - $container_name doesnot exists!"
fi

# 启动容器，jar包放在宿主机的 /Users/aborn/docker/packages 目录下
podman run --name codepulse-serv -it -v /Users/aborn/docker/packages:/packages -d -p 8001:8001 --net=codepulse_net codepulse_serv:v1


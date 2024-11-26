#!/usr/bin/env bash

# 构建数据库的docker镜像 （含有初始化数据）
docker build -t codepulse_db:v1 .

# 创建虚拟网络
network_name="codepulse_net"

# 检查网络是否存在
if ! docker network ls | grep -q "$network_name"; then
    # 如果网络不存在，则创建网络
    echo "Creating network: $network_name"
    docker network create "$network_name"
else
    echo "Network $network_name already exists."
fi

# 容器存在，则删除
container_name="codepulse_db"
if [ "$(docker ps -qa -f name=$container_name)" ]; then
    echo ":: Found container - $container_name"
    if [ "$(docker ps -q -f name=$container_name)" ]; then
        echo ":: Stopping running container - $container_name"
        docker stop $container_name;
    fi
    echo ":: Removing stopped container - $container_name"
    docker rm $container_name;
else
    echo ":: container - $container_name doesnot exists!"
fi

# 创建数据库容器
docker run -itd --name codepulse_db -p 3308:3306  --net=codepulse_net codepulse_db:v1
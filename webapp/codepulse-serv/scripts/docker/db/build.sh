#!/usr/bin/env bash

# 构建数据库的docker镜像 （含有初始化数据）
docker build -t codepulse_db:v1 .

# 创建一个网络
docker network create codepulse_net

# 创建数据库容器
docker run -itd --name codepulse_db -p 3308:3306  --net=codepulse_net codepulse_db:v1
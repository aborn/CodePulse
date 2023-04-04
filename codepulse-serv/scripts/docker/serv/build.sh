# 构建后端服务的docker镜像, FROM openjdk:17-oracle
docker build -t codepulse_serv:v1 .

# 启动容器，jar包放在宿主机的 /Users/aborn/docker/packages 目录下
docker run --name codepulse_serv -it -v /Users/aborn/docker/packages:/packages -d -p 8001:8001 codepulse_serv:v1


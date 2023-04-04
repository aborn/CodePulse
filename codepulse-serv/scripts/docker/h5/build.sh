# 构建后端服务的docker镜像, FROM openjdk:17-oracle
docker build -t codepulse_h5:v1 .

# 启动容器，dist文件包放在宿主机的 /Users/aborn/docker/h5/codepulse 目录下
# cp -r dist/ ~/docker/h5/codepulse/
docker run --name codepulse_h5 -it -v /Users/aborn/docker/h5/codepulse:/usr/share/nginx/html -d -p 8002:80 --net=codepulse_net codepulse_h5:v1
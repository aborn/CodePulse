# 构建数据库的docker镜像 （含有初始化数据）
docker build -t codepulse_db:v1 .

# 创建容器
docker run -itd --name codepulse -p 3308:3306 codepulse_db:v1
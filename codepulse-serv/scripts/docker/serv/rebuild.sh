#!/usr/bin/env bash
########################################
# 后端代码更新后重新运行下该脚本
# /Users/aborn/github/CodePulse/codepulse-serv/scripts/rebuild.sh
#########################################

DIR="$( cd "$( dirname "$0" )" && pwd )"

cd $DIR
cd ../../../

# 打jar包
mvn clean
mvn package

# 将jar包copy到指定目录
cp ./target/codepulse-serv-0.0.1-SNAPSHOT.jar /Users/aborn/docker/packages

# 重启动容器，让jar包生效
docker restart codepulse-serv
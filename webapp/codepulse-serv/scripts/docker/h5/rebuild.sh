#!/usr/bin/env bash
########################################
# 后端代码更新后重新运行下该脚本
# /Users/aborn/github/CodePulse/codepulse-serv/scripts/rebuild.sh
#########################################

DIR="$( cd "$( dirname "$0" )" && pwd )"

cd $DIR
cd ../../../../codepulse-vue

# 打jar包
yarn build

# 将文件copy到指定目录
cp -r dist/ /Users/aborn/docker/h5/codepulse/

# 重启动容器，让jar包生效
docker restart codepulse_h5
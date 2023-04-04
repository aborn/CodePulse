#!/usr/bin/env bash

cd /usr/local/frp

# 服务端运行
nohup ./frpc -c ./frpc.ini > /dev/null 2> /dev/null &
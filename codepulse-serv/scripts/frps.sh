#!/usr/bin/env bash

cd /usr/local/frp

# 服务端运行
nohup sudo ./frps -c ./frps.ini > /dev/null 2> /dev/null &
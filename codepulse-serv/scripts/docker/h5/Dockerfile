# 前端页面配置
FROM nginx:latest
# COPY ./dist /usr/share/nginx/html/
COPY ./conf/default.conf /etc/nginx/conf.d/default.conf

# install ping command
RUN apt-get update -y && apt-get install -y iputils-ping
CMD bash

# 暴露端口号
# EXPOSE 8002
FROM openjdk:17-jdk-slim
# 设置时区环境变量，确保与宿主机时区一致
ENV TZ=Asia/Shanghai
EXPOSE 8001
ENTRYPOINT ["java","-Dspring.datasource.url=jdbc:mysql://codepulse_db:3306/codepulse?useSSL=false&useUnicode=true&characterEncoding=utf-8","-jar","/packages/codepulse-serv-0.0.1-SNAPSHOT.jar"]

# install ping command
RUN apt-get update -y && apt-get install -y iputils-ping
CMD bash
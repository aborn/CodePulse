
-- 创建用户
create user 'codepulse'@'%' identified by 'C0de123654@22';
-- 授权
-- ALTER USER 'maisy'@'%' IDENTIFIED WITH mysql_native_password BY '';
grant all privileges on *.* to 'codepulse'@'%';
-- 更新
flush privileges;
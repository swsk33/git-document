#!/bin/bash
/etc/init.d/ssh restart
nginx
sudo -u git java -jar \
-Dspring.datasource.url="jdbc:mysql://$MYSQL_HOST:$MYSQL_PORT/git_doc?serverTimezone=GMT%2B8" \
-Dspring.datasource.username=$MYSQL_USER \
-Dspring.datasource.password=$MYSQL_PASSWORD \
-Dspring.data.redis.host=$REDIS_HOST \
-Dspring.data.redis.port=$REDIS_PORT \
-Dspring.data.redis.password=$REDIS_PASSWORD \
-Dspring.rabbitmq.host=$RABBIT_MQ_HOST \
-Dspring.rabbitmq.port=$RABBIT_MQ_PORT \
-Dspring.rabbitmq.username=$RABBIT_MQ_USER \
-Dspring.rabbitmq.password=$RABBIT_MQ_PASSWORD \
-Dspring.mail.host=$EMAIL_SMTP_HOST \
-Dspring.mail.username=$EMAIL_USER \
-Dspring.mail.password=$EMAIL_PASSWORD \
git-document-*.jar
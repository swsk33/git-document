#!/bin/bash
/etc/init.d/ssh restart
nginx
echo 延迟${DELAY_START}秒以等待其它服务启动...
sleep $DELAY_START
exec sudo -u git java -jar \
	-Dspring.datasource.url="jdbc:postgresql://$POSTGRE_HOST:$POSTGRE_PORT/git_doc?TimeZone=Asia/Shanghai" \
	-Dspring.datasource.username=$POSTGRE_USER \
	-Dspring.datasource.password=$POSTGRE_PASSWORD \
	-Dspring.data.redis.host=$REDIS_HOST \
	-Dspring.data.redis.port=$REDIS_PORT \
	-Dspring.data.redis.password=$REDIS_PASSWORD \
	-Dspring.rabbitmq.host=$RABBIT_MQ_HOST \
	-Dspring.rabbitmq.port=$RABBIT_MQ_PORT \
	-Dspring.rabbitmq.username=$RABBIT_MQ_USER \
	-Dspring.rabbitmq.password=$RABBIT_MQ_PASSWORD \
	-Dio.github.swsk33.code-post.core.smtp-host=$EMAIL_SMTP_HOST \
	-Dio.github.swsk33.code-post.core.email=$EMAIL_USER \
	-Dio.github.swsk33.code-post.core.password=$EMAIL_PASSWORD \
	-Dcom.gitee.swsk33.git-doc.host-port=$HOST_SSH_PORT \
	git-document.jar

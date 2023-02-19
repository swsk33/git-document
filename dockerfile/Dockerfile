FROM swsk33/jre-17-nginx-debian
# 添加必要文件
ADD springboot.tar /app/
ADD application.yml /app/config/
ADD nginx.conf /etc/nginx/conf.d/default.conf
ADD start.sh /
# 初始化容器系统
RUN mkdir -p /app/log /app/external-resource/avatar/user /app/external-resource/cover/custom /git-doc \
	&& useradd -s /bin/bash -m git \
	&& chmod +x /start.sh \
	&& chown -R git:git /app /git-doc \
	&& apt update \
	&& apt install -y --no-install-recommends sudo openssh-server git \
	&& apt clean \
	&& rm -rf /usr/share/nginx/html/* /var/lib/apt/lists/*
# 添加前端网页资源
ADD vue.tar /usr/share/nginx/html/
# 工作目录
WORKDIR /app
# 环境变量
# 语言设置
ENV LANG=C.UTF-8
# MySQL环境变量配置
ENV MYSQL_HOST=127.0.0.1
ENV MYSQL_PORT=3306
ENV MYSQL_USER=root
ENV MYSQL_PASSWORD=123456
# Redis环境变量配置
ENV REDIS_HOST=127.0.0.1
ENV REDIS_PORT=6379
ENV REDIS_PASSWORD=""
# RabbitMQ环境变量配置
ENV RABBIT_MQ_HOST=127.0.0.1
ENV RABBIT_MQ_PORT=5672
ENV RABBIT_MQ_USER=root
ENV RABBIT_MQ_PASSWORD=123456
# 邮件配置
ENV EMAIL_SMTP_HOST=smtp.163.com
ENV EMAIL_USER=""
ENV EMAIL_PASSWORD=""
# 需映射至宿主机23端口
EXPOSE 22
# 配置数据卷
VOLUME ["/git-doc", "/app/log", "/app/config", "/app/external-resource/avatar/user", "/app/external-resource/cover/custom"]
CMD ["/start.sh"]
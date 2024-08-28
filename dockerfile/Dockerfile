FROM bellsoft/liberica-openjre-alpine:21
# 添加必要文件
ADD git-document.jar /app/
ADD start.sh /
# 初始化容器系统
RUN mkdir -p /git-doc /app/images \
	# 用户与权限配置
	&& adduser git -D -s /bin/sh \
	&& chmod +x /start.sh \
	&& chown -R git:git /app /git-doc \
	# 配置相关软件
	&& apk update \
	&& apk add --no-cache sudo ca-certificates \
	&& wget -P /etc/apk/keys/ https://nginx.org/keys/nginx_signing.rsa.pub \
	&& printf "%s%s%s%s\n" "@nginx " "http://nginx.org/packages/alpine/v" `egrep -o '^[0-9]+\.[0-9]+' /etc/alpine-release` "/main" | tee -a /etc/apk/repositories \
	&& apk update \
	&& apk add --no-cache nginx@nginx \
	&& rm -rf /usr/share/nginx/html/* /var/cache/apk/*
# 添加Nginx配置和前端网页资源
ADD nginx.conf /etc/nginx/conf.d/default.conf
ADD vue.tar /usr/share/nginx/html/
# 工作目录
WORKDIR /app
# 环境变量
# 语言设置
ENV LANG=C.UTF-8
# 数据库环境变量配置
ENV GITDOC_DB_HOST=127.0.0.1
ENV GITDOC_DB_PORT=5432
ENV GITDOC_DB_NAME=git_doc
ENV GITDOC_DB_USER=root
ENV GITDOC_DB_PASSWORD=123456
# Redis环境变量配置
ENV GITDOC_REDIS_HOST=127.0.0.1
ENV GITDOC_REDIS_PORT=6379
ENV GITDOC_REDIS_PASSWORD=""
# 邮件配置
ENV GITDOC_MAIL_SMTP_HOST=smtp.163.com
ENV GITDOC_MAIL_USER=""
ENV GITDOC_MAIL_PASSKEY=""
# 内嵌SSH服务器设置
ENV GITDOC_SSH_PORT=2333
ENV GITDOC_ADVERTISED_SSH_PORT=2333
# 文集仓库根目录设置
ENV GITDOC_REPO_PATH=/git-doc
# 延迟启动时间（秒）以等待数据库和中间件启动完毕
ENV DELAY_START=0
# 建议映射至宿主机22端口
EXPOSE 2333
# 配置数据卷
VOLUME ["/git-doc", "/app/images"]
CMD ["/start.sh"]
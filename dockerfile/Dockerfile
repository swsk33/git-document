FROM swsk33/jre-17-nginx-alpine
# 初始化容器系统
RUN mkdir -p /app/config /app/log /app/external-resource/avatar/user /app/external-resource/cover/custom /git-doc \
	&& adduser -D git \
	&& apk add --no-cache sudo openssh-server git \
	&& cd /etc/ssh \
	&& ssh-keygen -A \
	&& rm -rf /usr/share/nginx/html/* /var/cache/apk/*
# 添加必要文件
ADD springboot.tar /app/
ADD application.yml /app/config/
ADD vue.tar /usr/share/nginx/html/
ADD nginx.conf /etc/nginx/conf.d/default.conf
ADD start.sh /
# 调整文件权限
RUN chmod +x /start.sh \
	&& chown -R git:git /app /git-doc
WORKDIR /app
# 环境变量
ENV LANG C.UTF-8
# 需映射至宿主机23端口
EXPOSE 22
# 配置数据卷
VOLUME ["/git-doc", "/app/log", "/app/config", "/app/external-resource/avatar/user", "/app/external-resource/cover/custom"]
CMD ["/start.sh"]
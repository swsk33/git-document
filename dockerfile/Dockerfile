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
# 调整文件权限
WORKDIR /app
# 环境变量
ENV LANG C.UTF-8
# 需映射至宿主机23端口
EXPOSE 22
# 配置数据卷
VOLUME ["/git-doc", "/app/log", "/app/config", "/app/external-resource/avatar/user", "/app/external-resource/cover/custom"]
CMD ["/start.sh"]
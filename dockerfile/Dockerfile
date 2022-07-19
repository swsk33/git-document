FROM swsk33/openjdk-zulu-jre-17-debian:nginx
# 初始化容器系统
RUN rm -rf /usr/local/nginx/html/* /usr/local/nginx/conf/nginx.conf \
	&& mkdir /app /app/config /git-doc \
	&& useradd -s /bin/bash -m git \
	&& apt install -y sudo vim openssh-server git \
	&& apt clean \
	&& ln -s /opt/zulu-17/bin/java /usr/bin/java
# 添加必要文件
ADD springboot.tar.xz /app/
ADD vue.tar.xz /usr/local/nginx/html/
ADD application.properties /app/config/
ADD nginx.conf /usr/local/nginx/conf/
ADD start.sh /
# 调整文件权限
RUN chmod +x /start.sh \
	&& chmod -R 777 /app /git-doc
WORKDIR /app
# 需映射至宿主机23端口
EXPOSE 22
# 配置数据卷
VOLUME ["/git-doc", "/app/config", "/usr/local/nginx/conf"]
CMD ["/start.sh"]
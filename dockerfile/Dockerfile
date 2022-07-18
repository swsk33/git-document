FROM swsk33/openjdk-zulu-jre-17-alpine:nginx-lite
# 初始化容器系统
RUN rm -rf /usr/local/nginx/html/* /usr/local/nginx/conf/nginx.conf \
	&& mkdir /app \
	&& mkdir /app/config \
	&& adduser git -D \
	&& apk add sudo vim
# 添加必要文件
ADD springboot.tar.xz /app/
ADD vue.tar.xz /usr/local/nginx/html/
ADD application.properties /app/config/
ADD nginx.conf /usr/local/nginx/conf/
ADD start.sh /
# 调整文件
RUN chmod +x /start.sh \
	&& chmod -R 777 /app
WORKDIR /app
CMD ["/start.sh"]
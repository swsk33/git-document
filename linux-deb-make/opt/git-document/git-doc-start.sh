#!/bin/bash
# 该脚本为打包安装包时的启动脚本，放在和jar文件同级目录下

javaNotFound() {
	echo 找不到Java运行环境！请先安装JRE！
}

execStart() {
	echo 正在启动...
	# 先开启端口转发
	sudo sysctl -w net.ipv4.ip_forward=1
	sudo sysctl -p
	# 切换工作路径为jar所在路径
	cd /opt/git-document
	# 通过安装包安装时配置文件位于：/etc/git-document/application.properties
	java -jar git-document.jar --spring.config.location=/etc/git-document/
}

java -version || javaNotFound && execStart
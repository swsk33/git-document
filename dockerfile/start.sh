#!/bin/sh
nginx
if [ $DELAY_START -ne 0 ]; then
	echo 延迟${DELAY_START}秒以等待其它服务启动...
	sleep $DELAY_START
fi
exec sudo -u git -E java -jar git-document.jar

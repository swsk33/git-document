#!/bin/bash
version=$1

if [ -z "$version" ]; then
	echo 请指定版本号！
	exit
fi

echo 清理上次构建...
rm -f ./*.tar ./*.jar
echo 开始构建后端...
cd ../server-springboot
mvn clean package
cp -f ./target/git-document-*.jar ../dockerfile/git-document.jar
echo 后端构建完成！
echo 开始构建前端...
cd ../web-vue-vite
npm run build
7z a -ttar ../dockerfile/vue.tar ./dist/*
echo 前端构建完成！
echo 构建Dockerfile...
cd ../dockerfile
docker build --network host --build-arg ALL_PROXY="http://127.0.0.1:7500" -f Dockerfile -t swsk33/git-document:$version .
echo 创建latest标签...
docker tag swsk33/git-document:$version swsk33/git-document
echo 全部构建完成！

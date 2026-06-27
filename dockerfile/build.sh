#!/bin/bash
app_version=$1

if [ -z $app_version ]; then
	echo 请指定版本号！
	exit 1
fi

echo 开始构建后端...
cd ../server-springboot
mvn clean package
cp -f ./app/target/git-document-app-${app_version}.jar ../dockerfile/git-document.jar
echo 后端构建完成！

echo 开始构建前端...
cd ../web-vue-vite
npm run build
tar -cvf ../dockerfile/vue.tar -C ./dist/ .
echo 前端构建完成！

echo 构建Dockerfile...
cd ../dockerfile
docker build --network=host \
	--build-arg http_proxy="http://127.0.0.1:7500" \
	--build-arg https_proxy="http://127.0.0.1:7500" \
	-f Dockerfile \
	-t swsk33/git-document:$app_version .
echo 创建latest标签...
docker tag swsk33/git-document:$app_version swsk33/git-document

echo 全部构建完成！
echo 清理构建...
rm ./vue.tar ./git-document.jar
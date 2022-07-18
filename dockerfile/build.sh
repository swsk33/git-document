#!/bin/bash
# 开发完成后的构建应用脚本，构建完成后才能构建Dockerfile
echo 清理上次构建...
rm -rf ./*.tar.xz
echo 开始构建后端...
cd ../server-springboot
mvn clean package
7z a -ttar ../dockerfile/springboot.tar ./target/git-document-*.jar ./external-resource
echo 后端构建完成！
echo 开始构建前端...
cd ../web-vue-vite
npm run build
7z a -ttar ../dockerfile/vue.tar ./dist/*
echo 前端构建完成！
echo 压缩构建结果...
cd ../dockerfile
7z a -txz -mx9 ./springboot.tar.xz springboot.tar
7z a -txz -mx9 ./vue.tar.xz vue.tar
echo 压缩构建完成！
echo 正在清理...
rm -rf ./*.tar
echo 清理完成！
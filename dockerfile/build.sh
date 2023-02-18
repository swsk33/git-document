#!/bin/bash
# 开发完成后的构建应用脚本，构建完成后才能构建Dockerfile
echo 清理上次构建...
rm -rf ./*.tar
echo 开始构建后端...
cd ../server-springboot
mvn clean package
7z a -ttar ../dockerfile/springboot.tar ./target/git-document-*.jar ./external-resource '-xr!external-resource/avatar/user/' '-xr!external-resource/cover/custom/' '-xr!external-resource/background/custom.jpg' '-xr!external-resource/login-background/custom.jpg'
echo 后端构建完成！
echo 开始构建前端...
cd ../web-vue-vite
npm run build
7z a -ttar ../dockerfile/vue.tar ./dist/*
echo 前端构建完成！
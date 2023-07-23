# GitDocument

## 1，介绍
程序员都是一群喜欢钻研技术的人，对于大多数程序员来说，他们并不喜欢追求花里胡哨的样式，而是更加注重内容本身，简洁即最好，优雅为最先。正是这样，Markdown成为了程序员们的主流，大多数文档博客等等，都是以Markdown形式编写和管理。

对于程序员组成的小型项目团队，文档的管理也至关重要，对于每个程序员来说，Git版本管理工具，也是程序员们必备的工具了！因此，采用Git + Markdown形式的文档管理也成为了许多人的选择。

这样，我们通常会将Github或者自建的Gitlab作为文档的管理和储存仓库并在线预览，然而对于Github、Gitlab这样的仓库托管平台来说，他们更加注重代码的托管、版本的管理等等，虽然他们有文档预览的功能，但是对于经常需要来看文档的人来说，其用户体验并不是特别的让人满意。

因此，我们开发了这个**面向小型程序员团队的文档管理和在线浏览平台**——**GitDocument**。

GitDocument是一个轻量的团队文档管理和查看工具，部署之后，完全使用Git来管理团队的文档，并在线浏览，文档全部为Markdown文件形式。

**登录界面**

![image-20230722233254043](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/undefinedimage-20230722233254043.png)

**主界面**
![image-20230722233320954](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/undefinedimage-20230722233320954.png)

**在线文档目录**

![image-20230722233424397](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/undefinedimage-20230722233424397.png)

**文档浏览界面（白天主题）**

![image-20230722233520467](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/undefinedimage-20230722233520467.png)

**文档浏览界面（夜晚主题）**

![image-20230722233531742](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/undefinedimage-20230722233531742.png)

### (1) 主要功能

- 基于Git和Markdown的文档管理
	- 创建Git仓库作为在线文集
	- 完全以Git仓库的形式管理在线文集，管理员可以克隆文集仓库到本地
	- 编写好文档后即可推送至文集仓库并在线浏览
	- 贡献者查看
	- 文集收藏功能（`star`）
- 用户登录及其基本管理功能
- 基本系统设置及其简单自定义

### (2) 待开发的功能

- 在线多版本和分支查看
- 手机端适配
- 文章评论
- ...

### (3) 软件架构

该程序前端使用Vue 3框架进行开发，使用Vite作为构建工具，用到了以下开源的外部库：

- [axios](https://axios-http.com/) 网络请求
- [clipboard.js](https://clipboardjs.com/) 复制内容
- [element-plus](https://element-plus.gitee.io/zh-CN/) 组件库及其图标库
- [marked.js](https://marked.js.org/) Markdown转HTML
- [highlight.js](https://highlightjs.org/) 实现代码高亮
- [katex](https://katex.org/) 对LaTeX公式支持
- [sass](https://sass-lang.com/) CSS预处理器
- [Vue-Router](https://router.vuejs.org/zh/) 单页路由
- [Pinia](https://pinia.vuejs.org/zh/) 状态管理

后端是使用Spring Boot进行搭建，使用PostgreSQL作为数据库，可以理解为是SSM架构，除了Spring Boot的一些Starter之外，还用到了以下开源外部库或者是中间件：

- [Sa-Token](https://sa-token.dev33.cn/) 认证和鉴权框架
- [ReadAndWriteJ](https://gitee.com/swsk33/ReadAndWriteJ) 读写实用类
- [JGit](https://www.eclipse.org/jgit/) 用于使用Java来操作Git仓库
- [Hutool](https://hutool.cn/) 实用工具集
- 集成[Redis](https://redis.io/)作为缓存功能
- 集成[RabbitMQ](https://www.rabbitmq.com/)作为任务和邮件通知消息队列

### (4) 说在前面

由于开发者时间紧迫，因此现版本的GitDocument功能并非太完善，也可能会有一些bug，敬请期待，欢迎大家提供自己宝贵的建议，有bug请及时在issue中发出。

如果这个软件帮助到了你，请点上你宝贵的`star`，非常感谢！

该项目托管在如下仓库：[Gitee](https://gitee.com/swsk33/git-document) | [Github](https://github.com/swsk33/git-document)

## 2，文档

该平台的部署和使用文档可以通过下列链接查看：

- 部署手册：[传送门](./doc/部署手册.md)
- 使用手册：[传送门](./doc/使用手册.md)
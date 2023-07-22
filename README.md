# GitDocument

## 1，介绍
程序员都是一群喜欢钻研技术的人，对于大多数程序员来说，他们并不喜欢追求花里胡哨的样式，而是更加注重内容本身，简洁即最好，优雅为最先。正是这样，Markdown成为了程序员们的主流，大多数文档博客等等，都是以Markdown形式编写和管理。

对于程序员组成的小型项目团队，文档的管理也至关重要，对于每个程序员来说，Git版本管理工具，也是程序员们必备的工具了！因此，采用Git + Markdown形式的文档管理也成为了许多人的选择。

这样，我们通常会将Github或者自建的Gitlab作为文档的管理和储存仓库并在线预览，然而对于Github、Gitlab这样的仓库托管平台来说，他们更加注重代码的托管、版本的管理等等，虽然他们有文档预览的功能，但是对于经常需要来看文档的人来说，其用户体验并不是特别的让人满意。

因此，我们开发了这个**面向小型程序员团队的文档管理和在线浏览平台**——**GitDocument**。

GitDocument是一个轻量的团队文档管理和查看工具，部署之后，完全使用Git来管理团队的文档，并在线浏览，文档全部为Markdown文件形式。

![image-20220630113713174](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630113713174.png)

<center>登录界面</center>

![image-20220630113903462](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630113903462.png)

<center>主界面</center>

![image-20220630114047551](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630114047551.png)

<center>在线文档目录</center>

![image-20220630114122302](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630114122302.png)

<center>文档浏览界面（白天主题）</center>

![image-20220630114424710](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630114424710.png)

<center>文档浏览界面（夜晚主题）</center>

### (1) 主要功能

- 基于Git和Markdown的文档管理
	- 创建Git仓库作为在线文集
	- 完全以Git仓库的形式管理在线文集，管理员可以克隆文集仓库到本地
	- 编写好文档后即可推送至文集仓库并在线浏览
	- 贡献者查看
	- 文集收藏功能（`star`）
- 用户管理
- 基本系统设置

### (2) 待开发的功能

- 在线多版本和分支查看
- 错误报告
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

后端是使用Spring Boot进行搭建，使用MySQL作为数据库，可以理解为是SSM架构，除了Spring Boot的一些Starter之外，还用到了以下开源外部库或者是中间件：

- [Sa-Token](https://sa-token.dev33.cn/) 认证和鉴权框架
- [ReadAndWriteJ](https://gitee.com/swsk33/ReadAndWriteJ) 读写实用类
- [yitter-idgenerator](https://github.com/yitter/idgenerator) 雪花id生成器
- [jackson-annotations](https://github.com/FasterXML/jackson-annotations) JSON注解实用工具
- [common-lang3](https://commons.apache.org/proper/commons-lang/) 实用工具
- [common-io](https://commons.apache.org/proper/commons-io/) 读写实用工具，用于仓库文件系统监听
- [JGit](https://www.eclipse.org/jgit/) 用于使用Java来操作Git仓库
- 集成[Redis](https://redis.io/)作为缓存功能
- 集成[RabbitMQ](https://www.rabbitmq.com/)作为任务和邮件通知消息队列

### (4) 说在前面

由于开发者时间紧迫，因此现版本的GitDocument功能并非太完善，也可能会有一些bug，敬请期待，欢迎大家提供自己宝贵的建议，有bug请及时在issue中发出。

如果这个软件帮助到了你，请点上你宝贵的`star`，非常感谢！

该项目托管在如下仓库：[Gitee](https://gitee.com/swsk33/git-document) | [Github](https://github.com/swsk33/git-document)



## 3，使用说明

安装配置完成之后，访问服务器地址或者域名即可进入网页。

### (1) 角色权限

平台中的用户分为两个角色，其权限如下：

- 管理员：
	- 创建文集仓库并用Git推送文章
	- 修改文集仓库信息、删除文集仓库
	- 增加用户权限、删除用户、重置用户密码
	- 修改系统设置
- 团队成员：
	- 浏览内部文章

在初始化数据库之后，会有一个默认的内置管理员账户，其用户名密码如下：

- 用户名：`admin`
- 密码：`789101112`

初次需要使用这个管理员用户登录，并修改一下自己的一些信息。

### (2) 界面介绍

登录页面非常简单，登录进入即可，登录后就进入了主面板页面。

登录后，主面板如下图：

![image-20230218230807362](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/undefinedimage-20230218230807362.png)

### (3) 新建文集并推送文章

只有**管理员账户**拥有对内部文集仓库的创建、修改、推送等等权限。

#### 1. 生成SSH密钥对

在推送文章之前，需要先配置SSH密钥，否则无法推送。**如果你之前已经生成过，就可以跳过这一步。**

在自己的电脑上打开Git Bash，输入以下命令生成密钥对：

```bash
ssh-keygen -t rsa -C "密钥名"
```

密钥名自己取，可以是邮箱也可以是随意的命名。然后连按三次回车，密钥对就生成了！

在`C:\Users\你的用户名\.ssh`目录下可以看到生成的密钥文件：

![image-20220630163405429](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630163405429.png)

#### 2. 配置公钥到你的账户

点击右上角用户头像 - 个人设置：

![image-20220630163455315](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630163455315.png)

在这里你可以设定你的个人信息，当然现在我们需要设定一下公钥，点击下方添加SSH公钥的按钮，把刚刚生成的公钥文件的内容粘贴进去。

先用文本编辑器例如VSCode打开你的公钥文件`id_rsa.pub`，全选复制里面的内容，然后点击个人设置中的添加SSH公钥，把内容粘贴进去：

![image-20220630163709438](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630163709438.png)

如果提示“公钥内容不合法！”的错误，请检查**内容末尾是否有回车**，去掉回车即可。

到此，公钥配置完毕！

#### 3. 创建文集仓库并推送文章

每一个文集，**其实质上就是一个Git仓库**，和我们在Github上面创建的仓库没有什么区别，你可以向往常一样向里面推送内容，克隆它等等，**只不过我们的文集仓库存放的都是Markdown文档而非代码**。

在内部文章集功能区，点击右上角增加文集按钮，填写文集信息：

![image-20220630162802924](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630162802924.png)

![image-20220630162855326](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630162855326.png)

创建完成，点击复制Git SSH地址按钮，即可复制这个文集仓库的远程仓库地址。

然后在你本地建立一个文件夹，其中放你需要共享到GitDocument上的文档，都需要是Markdown格式，**把这个文件夹作为一个Git仓库，并给其配置文集仓库的远程地址**：

![image-20220630164742763](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630164742763.png)

在文件夹中执行以下命令：

```bash
# 文集仓库就和普通仓库没区别，只不过我们的文集仓库存放的都是Markdown文档而非代码
git init
git remote add origin 你复制的文集SSH地址
# 三步曲，大家都熟悉的
git add .
git commit -m "first commit"
git push origin master
```

推送内容之后，点击文集的去阅读按钮，即可看到文集中的文章列表了！

![image-20220630165125880](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630165125880.png)

选择其中的文集即可阅读。

以后可以对本地仓库的文档进行修改，然后进行推送，更新GitDocument中的内容。

对于文集仓库有以下注意事项：

- GitDocument只会读取并显示文集仓库中头指针`HEAD`所在的`commit`的内容，后续会推出多版本预览的功能
- 文集仓库中除了Markdown类型的文件（扩展名为`.md`的文件）之外的文件都不会显示
- Markdown文档中的图片**可以使用网络图片**，**也可以使用相对路径的图片**
- GitDocument目前不提供在线文章编辑的功能，请在本地利用Markdown编辑器例如Typora、Marktext等等工具进行编辑然后推送上来

### (4) 管理员用户管理

管理员账户可以管理内部的用户，点击主面板旁边的用户管理功能，如下：

![image-20230218231001804](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/undefinedimage-20230218231001804.png)

点击右上角的添加用户，即可添加一个用户：

![image-20220630170009289](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630170009289.png)

![image-20220630170053817](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630170053817.png)

这个面板是可以上下滚动的。

在用户管理列表中，调整对应用户的角色字段的下拉框，即可更改该用户的角色：

![image-20220630170156795](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630170156795.png)

若有用户需要重置密码，点击对应用户的右侧的重置密码按钮即可，重置密码后，会有一封邮件发到被重置密码的用户的邮箱中，里面会有该用户重置后的密码，请管理员及时提醒该用户登录并修改密码。

若要删除用户，点击右侧删除按钮即可。

### (5) 系统设置

点击左侧系统设置，即可进行整个系统进行个性化设置，同样地，只有管理员才能进行系统设置。

![image-20230218231158896](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/undefinedimage-20230218231158896.png)

### (6) 文章阅读

在点击文集的去阅读按钮即可进入文集列表，点击要阅读的文章进行阅读。

文章界面结构如下：

![image-20220630170918326](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630170918326.png)

![image-20220630171016713](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630171016713.png)

### (7) LaTeX公式问题

从`1.3.0`版本起，GitDocument添加了对LaTeX公式的支持，不过需要注意以下注意事项：

- 块状公式换行请不要用`\\`，而是使用`\newline`，否则会导致换行不生效
- LaTeX公式的支持是使用katex库实现，但是katex并非支持所有的LaTeX语法，大部分都支持，支持列表请参考：
	- 支持的函数：[传送门](https://katex.org/docs/supported.html)
	- 支持的语法：[传送门](https://katex.org/docs/support_table.html)

## 4，其余配置项

在GitDocument中，也提供了其它的一些配置项，主要是通过修改**Spring Boot配置文件**完成，在上面挂载数据卷的时候就已经得知了配置文件位置了。

Spring Boot的配置文件是[YAML](https://yaml.org/)格式的，若语法不太熟悉可以先参考：[菜鸟教程](https://www.runoob.com/w3cnote/yaml-intro.html)。

在Spring Boot配置文件中，也有相应的注释，根据注释可以快速定位到需要修改的项，配置完成后记得重启容器。

### (1) 组织名

访问登录页面时，标题会显示组织名，包括网页标题。这个是可以自定义的，编辑配置文件即可，配置文件中`com.gitee.swsk33.git-doc.organization-name`即为自定义的组织名，配置文件位置在上面两种安装方式中已经提到过了。

修改后记得重启服务。

### (2) 是否允许公开注册

默认情况下GitDocument平台是允许访客注册的，若内部的内容不能对外开放，可以关闭公开注册，这时只有管理员可以创建账号。

修改配置项`com.gitee.swsk33.git-doc.allow-public`为`false`即可关闭访客注册。

> 最后更新：2023.2.19

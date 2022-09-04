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
- 用户管理

### (2) 待开发的功能

- 文集收藏（star）
- 在线多版本和分支查看
- 错误报告
- 手机端适配
- 文章评论
- ...

### (3) 软件架构

该程序前端使用Vue 3框架进行开发，使用Vite作为构建工具，用到了以下开源的外部库：

- [axios](https://axios-http.com/) 网络请求
- [clipboard.js](https://clipboardjs.com/) 复制内容
- [element-plus](https://element-plus.gitee.io/zh-CN/) 组件库
- [marked.js](https://marked.js.org/) Markdown转HTML
- [highlight.js](https://highlightjs.org/) 实现代码高亮
- [katex](https://katex.org/) 对LaTeX公式支持
- [sass](https://sass-lang.com/) CSS预处理器
- [Vue-Router](https://router.vuejs.org/zh/) 单页路由
- [Vuex](https://vuex.vuejs.org/zh/) 状态管理

后端是使用Spring Boot进行搭建，可以理解为是SSM架构，除了Spring Boot的一些Starter之外，还用到了以下开源外部库：

- [Sa-Token](https://sa-token.dev33.cn/) 认证和鉴权框架
- [ReadAndWriteJ](https://gitee.com/swsk33/ReadAndWriteJ) 读写实用类
- [yitter-idgenerator](https://github.com/yitter/idgenerator) 雪花id生成器
- [jackson-annotations](https://github.com/FasterXML/jackson-annotations) JSON注解实用工具
- [common-lang3](https://commons.apache.org/proper/commons-lang/) 实用工具
- [common-io](https://commons.apache.org/proper/commons-io/) 读写实用工具，用于仓库文件系统监听
- [JGit](https://www.eclipse.org/jgit/) 用于使用Java来操作Git仓库

### (4) 说在前面

由于开发者是苦逼考研人，时间紧迫，因此现在较早版本的GitDocument功能并非太完善，也可能会有一些bug，敬请期待，欢迎大家提供自己宝贵的建议，有bug请及时在issue中发出。

如果这个软件帮助到了你，请点上你宝贵的star，非常感谢！

该项目托管在如下仓库：[Gitee](https://gitee.com/swsk33/git-document) | [Github](https://github.com/swsk33/git-document)

## 2，安装和部署

该项目需要安装部署在Linux服务器上，大家可以通过拉取Docker镜像的方式运行。

若你要编译运行与实体机上，则需要满足以下系统要求：

- 操作系统：Linux操作系统，推荐Debian 11
- 运行环境：Java 17及其以上版本
- 数据库：MySQL 8及其以上版本
- 工具：服务端需安装Git

### (1) 安装和配置数据库

该平台需要MySQL 8及其以上版本的数据库支持，安装GitDocument之前需要先配置好数据库，这里不再赘述MySQL的安装配置过程，若不太熟悉可以先参考这个文章进行MySQL的安装和配置：[传送门](https://juejin.cn/post/6989125524937244686)

安装完成MySQL之后，需要建立数据库并初始化数据，先连接你的MySQL，可以创建一个数据库用户，也可以直接用`root`用户，然后建立一个名为`git_doc`的数据库：

```sql
-- 创建数据库
create database `git_doc`;
-- 进入数据库
use `git_doc`;
```

然后下载这个初始化用的`sql`文件：[点击下载](https://gitee.com/swsk33/git-document/raw/master/server-springboot/sql/init-data.sql)

若点击下载后文件没有下载而是直接被浏览器打开，请尝试鼠标右键点击下载链接 - 链接另存为... 以下载。

下载完成，执行这个文件：

```sql
source 下载的sql文件位置
```

若执行过程报错，请检查终端字符集是否是`UTF-8`，也可以打开文件将所有内容复制粘贴进终端执行。

到此，数据库初始化完成！

### (2) 安装和运行

这里提供**通过Docker镜像部署运行**和**源码编译安装**两种方式，两种方式都写在这里，大家可以根据实际情况自行选择其中一种方式。

#### 1. 通过拉取Docker镜像（推荐）

使用Docker镜像的方式部署的话，请先确保服务器的`80`、`443`和`23`端口可以被外网正常访问，即需要确保这几个端口加入到服务器防火墙白名单。

##### ① 安装Docker Engine并拉取镜像

首先连接服务器，并在服务器上安装好Docker Engine，这部分不再赘述，请参考官方文档：[传送门](https://docs.docker.com/engine/install/debian/)

安装完成Docker Engine之后，拉取最新版GitDocument镜像：

```bash
docker pull swsk33/git-document
```

##### ② 创建容器并修改配置文件

拉取镜像并创建容器之前，为了方便我们修改配置文件，我们先要为容器内的Spring Boot和Nginx的配置文件目录创建数据卷：

```bash
docker volume create git-doc-springboot
docker volume create git-doc-nginx
```

然后创建容器并挂载数据卷：

```bash
docker run -id --name=git-doc -p 80:80 -p 443:443 -p 23:22 -v git-doc-springboot:/app/config -v git-doc-nginx:/usr/local/nginx/conf swsk33/git-document
```

这样，便创建好了容器。由于是第一次创建容器，但是一些关键配置例如数据库等等还没配置，这时程序是没有正常运行的，因此我们需要修改关键配置。

按照上述步骤挂载数据卷之后，配置文件应当位于宿主机如下位置：

- **Spring Boot配置文件**：`/var/lib/docker/volumes/git-doc-springboot/_data/application.properties`
- **Nginx配置文件**：`/var/lib/docker/volumes/git-doc-nginx/_data/nginx.conf`

可以通过vim等等文本编辑器编辑配置，下列配置修改都是基于这两个文件。

###### 1) MySQL数据库（必须）

这一项配置是修改**Spring Boot配置文件**。

- `spring.datasource.url` 数据库地址，把`127.0.0.1:3306`这部分替换成你自己数据库的地址和端口，若数据库和GitDocument部署在一起且端口没改的话则无需修改
- `spring.datasource.username` 数据库用户名
- `spring.datasource.password` 数据库用户对应的密码

###### 2) 邮箱（必须）

这一项配置是修改**Spring Boot配置文件**。

用于发送通知邮件，需要先去注册一个邮箱例如QQ、163等等，并开启SMTP服务。

- `spring.mail.host` 邮箱smtp服务器地址，可以在对应的邮箱网站找到
- `spring.mail.username` 你的邮箱地址
- `spring.mail.password` 邮箱授权码（注意不是密码！）

###### 3) 开启https（非必须）

这一项配置是修改**Nginx配置文件**。

如果你需要开启https服务，你需要准备好**SSL证书文件**和**证书密钥文件**，然后修改Nginx配置。

首先准备好**证书文件**和**密钥文件**，可以通过`docker cp`命令把这两个文件拷贝到容器中再配置，也可以在上述`docker run`的时候通过`-v`参数配置数据卷，这里不再赘述。

在容器中同样通过`vim`编辑Nginx配置文件：

```bash
vim /usr/local/nginx/conf/nginx.conf
```

找到`80`端口对应的`server`块：

![image-20220707175202725](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220707175202725.png)

然后把`listen 80;`改成`listen 443 ssl;`，并在其下添加如下两行：

```nginx
ssl_certificate 你的证书文件路径;
ssl_certificate_key 你的证书密钥文件路径;
```

注意这里证书文件和密钥文件路径需要是在**容器内的路径**！

最终如下图：

![image-20220707175512156](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220707175512156.png)

然后再在这个`server`块上面再添加如下的`server`块用于80端口跳转443端口：

```nginx
# 80跳转443
server {
	listen 80;
	rewrite ^(.*)$ https://$host$1 permanent;
}
```


##### ③ 重新启动容器

修改完成所有配置后，需要重新启动容器：

```bash
docker restart git-doc
```

执行`docker ps -a`命令，若大致`15s`后容器状态能够一直保持运行状态则启动成功！

![image-20220719201932419](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220719201932419.png)

这个时候，访问服务器地址或者域名，就可以进入网页了！

##### ④ 容器关键目录说明

除了上述两个配置文件目录，容器中还有以下关键目录，在启动时会自动挂载至宿主机：

- `/git-doc` 存放文集Git仓库的位置
- `/app/external-resource` 存放一些静态资源的目录
- `/app/log` Spring Boot日志文件目录

可以通过下面命令查看这几个目录被挂载至哪个位置：

```bash
docker inspect git-doc
```

找到`Mounts`字段即可查看：

![image-20220719203927182](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220719203927182.png)

#### 2. 编译源码安装

使用源码安装的话，需要自行安装并配置Nginx。

在通过源码编译安装之前，建议大家在自己服务器上创建一个系统用户专门用于管理这些存放在服务器的文集Git仓库，我们就将这个用户命名为`git`就行。

> 事实上不创建这个系统用户，也不会影响你的使用，不过我们还是推荐创建一个专门的`git`用户，分离职责。

执行以下命令以创建用户：

```bash
# 创建git用户
sudo useradd -s /bin/bash -m git
# 给git用户设定密码
sudo passwd git
```

创建用户完成，即可进入我们的安装步骤了！

然后，我们还需要在服务器上面安装Git工具，大家可以直接通过命令安装：

```bash
sudo apt install git
```

通过`apt`安装的Git版本不是特别新，当然不影响使用。若想要安装最新Git可以从源码编译，参考：[传送门](https://juejin.cn/post/7083777705996451854)

##### ① 本地环境配置

在编译源码安装时，先要确保你的本地已安装并配置好如下环境：

- JDK 17及其以上版本
- Maven构建工具
- Node.js

可以执行下列命令以判断你的环境是否安装并配置好：

```bash
java -version
mvn -version
npm -v
```

若没有出现“xxx不是内部或者外部命令”这样的报错，则说明环境配置正确。

##### ② 构建Vue源代码

先将项目源代码都克隆下来，打开终端并切换当前路径到项目的`web-vue-vite`文件夹下，执行命令开始构建：

```bash
npm install
npm run build
```

构建完成，文件夹下会出现`dist`文件夹，这就是构建的结果。若`npm install`依赖安装的太慢，可以换用`cnpm`来执行安装依赖命令。

##### ③ 构建Spring Boot

打开终端，切换当前路径到项目目录的`server-springboot`目录下，执行命令以构建：

```bash
mvn clean package
```

等待构建完成，目录下会出现`target`目录即为构建结果。

##### ④ 上传Spring Boot构建结果到服务器

将项目目录的`server-springboot/target`目录下的`git-document-x.x.x.jar`（`x`为版本号）和`server-springboot`目录下的`config`目录与`external-resource`目录这三项一起上传到服务器，务必保证`jar`文件和这两个文件夹放在服务器上的同级目录下！

##### ⑤ 修改Spring Boot配置

上传到服务器后，编辑`config`目录下的`application.properties`文件，把`spring.profiles.active`的值由`dev`改为`prod`。

然后打开`config`目录下的`application-prod.properties`文件，这个就是主要配置文件，其中需要修改的配置项和上面的容器部署部分的配置文件是一样的，这里不再赘述，大家可以参考上面容器部署章节中的修改配置文件部分。

##### ⑥ 安装并配置Nginx

Nginx可以使用包管理器`apt`直接安装：

```bash
sudo apt install nginx
```

也可以自行编译安装，参考：[传送门](https://juejin.cn/post/7116382852836507679)

注意，两种安装方式的Nginx配置文件所在位置不同：

- `apt`安装：`/etc/nginx/conf/nginx.conf`
- 编译安装：`/usr/local/nginx/conf/nginx.conf`

先在配置文件中加入下列指令，指定以`root`用户运行Nginx：

```nginx
user root;
```

然后需要把上述第②步的Vue源码构建结果配置到Nginx中，把项目的`web-vue-vite/dist`文件夹下所有内容上传至服务器的某个目录下，这里假设：

- 前端构建结果传到服务器的`/root/web`目录下
- 把第④步的jar文件、`external-resource`和`config`目录上传到了服务器的`/root/app`目录下

那么就修改端口为`80`的`server`块如下：

```nginx
server {
	listen 80;
	server_name localhost;

	location / {
		root /root/web;
		index index.html;
		# 兼容Vue Router
		try_files $uri $uri/ /index.html;
	}

	# 转发请求
	# Spring Boot中端口默认是8800，若修改了Spring Boot端口，这里的端口对应也需要修改
	location /api {
		proxy_pass http://127.0.0.1:8800;
	}

	# 定位静态资源
	location /static {
		alias /root/app/external-resource;
	}
}
```

也就是说上述`location /`块中的`root`需要配置为Vue构建结果文件所在的目录，`location /static`块中`alias`需要对应为`external-resource`目录位置。

若要配置https，同样需要准备好SSL证书，然后先把上述`80`端口对应的`server`块改为如下：

```nginx
server {
	listen 443 ssl;
	ssl_certificate 你的证书文件路径;
	ssl_certificate_key 你的证书密钥文件路径;
	server_name localhost;

	# 下面所有location块和上面一样
}
```

然后再在这个`server`块的上面添加如下`server`块以实现http跳转https：

```nginx
# 80跳转443
server {
	listen 80;
	rewrite ^(.*)$ https://$host$1 permanent;
}
```

##### ⑦ 启动运行服务

首先是启动Nginx，确认Nginx正常启动后，再运行`jar`文件即可，注意运行路径需要在`jar`文件所在目录下！

```bash
# 先启动Nginx
sudo nginx
cd jar文件所在目录
# 以我们创建的git用户运行服务
sudo -u git java -jar git-document-x.x.x.jar
```

`x`是版本号，大家自行替换为自己的实际文件名。

建议大家通过`screen`软件将其挂在后台运行。

这个时候，访问服务器地址或者域名，就可以进入网页了！

## 3，平台使用

安装配置完成之后，访问服务器地址或者域名即可进入网页。

### (1) 角色权限

平台中的用户分为两个角色，其权限如下：

- 管理员：
	- 创建文集仓库并用Git推送文章
	- 修改文集仓库信息、删除文集仓库
	- 增加用户权限、删除用户、重置用户密码
- 团队成员：
	- 浏览内部文章

在初始化数据库之后，会有一个默认的内置管理员账户，其用户名密码如下：

- 用户名：`admin`
- 密码：`789101112`

初次需要使用这个管理员用户登录，并修改一下自己的一些信息。

### (2) 界面介绍

登录页面非常简单，登录进入即可，登录后就进入了主面板页面。

登录后，主面板如下图：

![image-20220630162651287](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630162651287.png)

### (3) 新建文集并推送文章

只有管理员账户拥有对内部文集仓库的创建、修改、推送等等权限。

#### 1. 生成SSH密钥对

在推送文章之前，需要先配置SSH密钥，否则无法推送。**如果你之前已经配置过，就可以跳过这一步。**

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

- GitDocument只会读取并显示文集仓库中头指针HEAD所在的commit的内容，后续会推出多版本预览的功能
- 文集仓库中除了Markdown类型的文件（扩展名为`.md`的文件）之外的文件都不会显示，也尽量不要在文集仓库中放别的东西
- Markdown文档中的图片请使用网络图片，若使用相对路径会无法显示，目前暂不支持相对路径的图片在线预览，以后这个缺陷会完善
- GitDocument目前不提供在线文章编辑的功能，请在本地利用Markdown编辑器例如Typora、Marktext等等工具进行编辑然后推送上来

### (4) 管理员用户管理

管理员账户可以管理内部的用户，点击主面板旁边的用户管理功能，如下：

![image-20220630165937205](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630165937205.png)

点击右上角的添加用户，即可添加一个用户：

![image-20220630170009289](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630170009289.png)

![image-20220630170053817](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630170053817.png)

这个面板是可以上下滚动的。

在用户管理列表中，调整对应用户的角色字段的下拉框，即可更改该用户的角色：

![image-20220630170156795](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630170156795.png)

若有用户需要重置密码，点击对应用户的右侧的重置密码按钮即可，重置密码后，会有一封邮件发到被重置密码的用户的邮箱中，里面会有该用户重置后的密码，请管理员及时提醒该用户登录并修改密码。

若要删除用户，点击右侧删除按钮即可。

### (5) 文章阅读

在点击文集的去阅读按钮即可进入文集列表，点击要阅读的文章进行阅读。

文章界面结构如下：

![image-20220630170918326](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630170918326.png)

![image-20220630171016713](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220630171016713.png)

### (6) LaTeX公式问题

从`1.3.0`版本起，GitDocument添加了对LaTeX公式的支持，不过需要注意以下注意事项：

- 块状公式换行请不要用`\\`，而是使用`\newline`，否则会导致换行不生效
- LaTeX公式的支持是使用katex库实现，但是katex并非支持所有的LaTeX语法，大部分都支持，支持列表请参考：
	- 支持的函数：https://katex.org/docs/supported.html
	- 支持的语法：https://katex.org/docs/support_table.html

## 4，个性化配置

在GitDocument中，也提供了许多的个性化配置。

### (1) 组织名

访问登录页面时，标题会显示组织名，包括网页标题。这个是可以自定义的，编辑配置文件即可，配置文件中`com.gitee.swsk33.git-doc.organization-name`即为自定义的组织名，配置文件位置在上面两种安装方式中已经提到过了。

修改后记得重启服务。

如果要配置中文的组织名，需要把内容转换为Unicode形式再填进去，直接写中文到配置文件中会导致乱码，可以通过这个在线工具先把中文转换为Unicode：[传送门](https://c.runoob.com/front-end/3602/)

### (2) 是否允许公开注册

默认情况下GitDocument平台是允许访客注册的，若内部的内容不能对外开放，可以关闭公开注册，这时只有管理员可以创建账号。

修改配置项`com.gitee.swsk33.git-doc.allow-public`为`false`即可关闭访客注册。

### (3) 自定义登录页背景图

默认情况下，登录页背景图会随着一天时间的不同而更换，当然也是可以自定义的。

准备好自定义的背景图，需要是`jpg`格式，最好是`16:9`的尺寸，`1920 x 1080`或者`2560 x 1440`分辨率，并把该图片命名为`custom.jpg`放到程序所在目录的`external-resource/login-background`目录下。当这个目录下存在`custom.jpg`文件时，登录页就会显示为你自定义的背景图片。

若是使用Docker容器部署，可以通过`docker cp`命令把图片拷贝到容器中的`/app/external-resource/login-background`目录下。

### (4) 自定义主面板页面背景图

默认情况下，主面板页面背景图是根据季节而显示不同的图片，这也是可以自定义的。

同样地，准备好自定义的背景图，`jpg`格式，最好是`16:9`的尺寸，`1920 x 1080`或者`2560 x 1440`分辨率，并把该图片命名为`custom.jpg`放到程序所在目录的`external-resource/background`目录下。当这个目录下存在`custom.jpg`文件时，主面板页就会显示为你自定义的背景图片。

若是使用Docker容器部署，可以通过`docker cp`命令把图片拷贝到容器中的`/app/external-resource/background`目录下。

> 最后更新：2022.7.19

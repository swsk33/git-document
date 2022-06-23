<template>
	<!-- 主体 -->
	<div class="main-body">
		<!-- 正文内容 -->
		<div class="content" v-html="text" ref="content" @click="setMenuShow(false)"></div>
		<!-- 菜单 -->
		<div class="menu" v-if="menuShow">
			<el-tooltip placement="right" v-for="item in menuItems" :key="item.text" :content="item.text">
				<a :style="{paddingLeft: getItemIndentation(item.indentation)}" :href="item.anchor">{{ item.text }}</a>
			</el-tooltip>
		</div>
	</div>
</template>

<script>
import { marked } from 'marked';
import { createNamespacedHelpers } from 'vuex';
import { ElNotification } from 'element-plus';

import highlight from 'highlight.js';
import ClipBoard from 'clipboard';

// vuex模块
const { mapState: themeState, mapActions: themeActions } = createNamespacedHelpers('articlepagetheme');

marked.setOptions({
			renderer: new marked.Renderer(),
			highlight: function (code) {
				return highlight.highlightAuto(code).value;
			},
			pedantic: false,
			gfm: true,
			tables: true,
			breaks: false,
			sanitize: false,
			smartLists: true,
			smartypants: false,
			xhtml: false
		}
);

export default {
	data() {
		return {
			// 正文内容
			text: undefined,
			// 菜单项
			menuItems: []
		};
	},
	computed: {
		...themeState(['menuShow', 'menuParsed', 'isNight'])
	},
	methods: {
		...themeActions(['setMenuShow', 'setMenuParsedDone', 'setIsNight']),
		/**
		 * 解析标题生成目录树
		 */
		parseTitle() {
			const doms = this.$refs.content.children;
			// 用于记录最大节点的等级
			let maxLevel = 6;
			// 标题锚点索引
			let index = 0;
			// 逐个获取节点名nodeName属性，找出标题节点
			for (let dom of doms) {
				if (dom.nodeName.startsWith('H')) {
					// 解析当前标题的层级
					let level = parseInt(dom.nodeName.substring(1, 2));
					if (isNaN(level)) {
						continue;
					}
					if (level < maxLevel) {
						maxLevel = level;
					}
					// 设定当前标题id作为锚点
					dom.id = 'title-' + index;
					// 加入到目录列表
					this.menuItems.push({
						text: dom.innerText,
						level: level,
						anchor: '#' + dom.id
					});
					index++;
				}
			}
			// 根据最大标题设定缩进级别
			for (let item of this.menuItems) {
				item.indentation = item.level - maxLevel;
			}
			this.setMenuParsedDone();
		},
		/**
		 * 获得目录项的缩进长度，css值形式
		 * @param indentationLevel 该项的缩进级别
		 */
		getItemIndentation(indentationLevel) {
			return 'calc(8% + ' + indentationLevel * 7 + 'px)';
		},
		/**
		 * 改变代码样式
		 * @param isNight 是否改为夜晚样式
		 */
		changeCodeStyle(isNight) {
			const preDoms = this.$refs.content.querySelectorAll('pre');
			for (let item of preDoms) {
				if (isNight) {
					item.className = 'pre-night';
				} else {
					item.className = 'pre-day';
				}
			}
		},
		/**
		 * 在代码块中添加代码语言名和复制
		 */
		showCodeTypeAndCopy() {
			const pres = this.$refs.content.querySelectorAll('pre');
			let index = 0;
			for (let item of pres) {
				let codeBlock = item.querySelector('code');
				codeBlock.id = 'code-block-' + index;
				let languageName = document.createElement('div');
				languageName.innerText = codeBlock.className.substring(codeBlock.className.indexOf('-') + 1);
				languageName.className = 'code-language';
				let copy = document.createElement('div');
				copy.innerText = 'copy';
				copy.className = 'copy-button';
				copy.setAttribute('data-clipboard-target', '#' + codeBlock.id);
				// 插入到代码块中
				item.appendChild(languageName);
				item.appendChild(copy);
				index++;
			}
			// 实例化剪贴板对象
			const clipBoard = new ClipBoard('.copy-button');
			clipBoard.on('success', (e) => {
				ElNotification({
					title: '成功！',
					message: '复制成功！',
					type: 'success',
					position: 'top-left',
					duration: 750
				});
				// 清除文本选中状态
				e.clearSelection();
			});
			clipBoard.on('error', (e) => {
				ElNotification({
					title: '错误！',
					message: '复制失败！请联系开发者！',
					type: 'error',
					position: 'top-left',
					duration: 750
				});
				// 清除文本选中状态
				e.clearSelection();
			});
		}
	},
	watch: {
		isNight() {
			this.changeCodeStyle(this.isNight);
		}
	},
	mounted() {
		// 初始化文本内容
		this.text = marked('## 1，变量\n' +
				'\n' +
				'变量是编程语言中最常见的东西了。在JS中使用`var`、`let`和`const`关键字定义变量：\n' +
				'\n' +
				'```javascript\n' +
				'let name = \'Will Smith\';\n' +
				'```\n' +
				'这里就定义了名为`name`的变量并赋值。\n' +
				'\n' +
				'平时我们使用`let`定义变量，`const`用于定义常量，`var`现在不建议使用。\n' +
				'\n' +
				'使用`let`定义的变量可以再次赋值：\n' +
				'\n' +
				'```javascript\n' +
				'name = \'a\';\n' +
				'```\n' +
				'但是`const`定义的就不行了，因为`const`是定义常量的。\n' +
				'\n' +
				'可见这里定义变量很简单，直接使用`let`或者`const`即可，不需要规定其数据类型。因为js会根据你的赋值自动判断数据类型。\n' +
				'\n' +
				'在几乎任何程序设计中，**变量都是存放在计算机内存中的**，每个变量声明并赋值后，它就会在内存中有一个内存地址并占据一定空间，这一点大家需要清楚。\n' +
				'\n' +
				'在大学计算机基础这门课中事实上已经讲解了内存的简单概念，若大家学习过C语言以及里面的指针，也会对内存空间有一个更加清晰的认知。\n' +
				'\n' +
				'不过无论如何我们需要去了解内存的最基本的概念，这样有助于帮助我们后续很多概念的理解：\n' +
				'\n' +
				'- **内存空间**：通常就称作内存，可以理解为一个线性的空间，**专门用于存放程序运行的数据**，当然我们声明的变量都放在里面\n' +
				'- **内存地址**：表示存放在内存的数据位于**内存的哪个位置**\n' +
				'\n' +
				'先简单理解这两个概念即可。\n' +
				'\n' +
				'在JavaScript中，基本数据类型一共有以下几种：\n' +
				'\n' +
				'- `undefined` 未定义\n' +
				'- `Boolean` 布尔值\n' +
				'- `Number` 数值\n' +
				'- `String` 字符串\n' +
				'- `BigInt` 长整型\n' +
				'- `Symbol` ES6中新增的符号类型\n' +
				'- `null` 空\n' +
				'\n' +
				'JavaScript是一门弱类型语言，因此声明变量时并不需要指明数据类型，都是用`let`或者`const`声明一个变量，运行的时候**会根据我们赋的值来决定这个变量的类型**。\n' +
				'\n' +
				'## 2，数值类型\n' +
				'\n' +
				'数值一般是**整数**、**浮点数**和**非数值**(`NaN`)。\n' +
				'\n' +
				'整数：\n' +
				'\n' +
				'```javascript\n' +
				'let num = 8;\n' +
				'```\n' +
				'浮点数：\n' +
				'```javascript\n' +
				'let float = 0.5;\n' +
				'```\n' +
				'科学计数法：\n' +
				'```javascript\n' +
				'let big = 9.3e7; // 9.3x10^7\n' +
				'let small = 9.3e-7; // 9.3x10^-7 \n' +
				'```\n' +
				'**NaN**\n' +
				'\n' +
				'即为`Not a Number`，表示一个非数值的占位符。\n' +
				'\n' +
				'一般来说，对两个变量执行了个运算，返回的是数值类型但是计算没有成功，就会返回`NaN`。\n' +
				'\n' +
				'例如除数为0、字符串和数字相乘等等就会出现这种情况。\n' +
				'\n' +
				'除此之外，对于特别特别大的数，可以在数值后面加一个`n`，表示这个变量是`BigInt`类型：\n' +
				'\n' +
				'```javascript\n' +
				'let bigNum = 9007199254740991n;\n' +
				'```\n' +
				'\n' +
				'## 3，布尔类型\n' +
				'\n' +
				'布尔型变量事实上是一种非常简单的变量。\n' +
				'\n' +
				'在生活中，我们常常遇到只需要选**是**或**否**的场景，比如说有人问你：今天晚上去不去海底捞？你会回答他**去**或者**不去**。\n' +
				'\n' +
				'在程序设计中，布尔型变量就是用于表示是或者否的，在程序中通常表示为`true`或者`false`（**真**或**假**）。\n' +
				'\n' +
				'在JavaScript中，也是用`true`和`false`表示布尔值：\n' +
				'\n' +
				'```javascript\n' +
				'let yes = true;\n' +
				'let no = false;\n' +
				'```\n' +
				'\n' +
				'上述我们就声明了两个布尔型的变量。\n' +
				'\n' +
				'## 4，字符串\n' +
				'\n' +
				'字符串在任何编程语言中都有着广泛的应用，这里不过多赘述。使用英文双引号`"`或者英文单引号`\'`包围起来的就是字符串，大多数情况下编写js时使用英文单引号包围字符串：\n' +
				'\n' +
				'```javascript\n' +
				'\'字符串\'\n' +
				'"字符串"\n' +
				'```\n' +
				'### (1) 字符串的声明和赋值\n' +
				'\n' +
				'我们声明一个字符串变量试试：\n' +
				'\n' +
				'```javascript\n' +
				'let str = \'abcd\';\n' +
				'```\n' +
				'\n' +
				'上述变量`str`就是一个字符串变量。\n' +
				'\n' +
				'### (2) 字符串的拼接\n' +
				'\n' +
				'字符串的拼接使用`+`即可。\n' +
				'\n' +
				'```javascript\n' +
				'let str1 = \'我是\';\n' +
				'let str2 = \'一个程序员\';\n' +
				'console.log(str1 + str2);\n' +
				'```\n' +
				'\n' +
				'![image-20220531204705989](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220531204705989.png)\n' +
				'\n' +
				'可见利用`+`号，可以把两个字符串进行拼接。\n' +
				'\n' +
				'### (3) 模板字符串\n' +
				'\n' +
				'模板字符串允许我们把一个变量插入到一个字符串值中，有点类似我们SCSS中的插值：\n' +
				'\n' +
				'```javascript\n' +
				'let name = \'Alex\';\n' +
				'let msg = `大家好，我叫${name}`;\n' +
				'console.log(msg);\n' +
				'```\n' +
				'![image-20220531204903952](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220531204903952.png)\n' +
				'\n' +
				'注意模板字符串要用反引号包围，反引号就是键盘左上角那个(~和`)。\n' +
				'\n' +
				'> 在JavaScript中，字符串使用单引号或者双引号包围都是一样的，但是建议大家统一规范，统一使用**单引号**包围字符串。\n' +
				'\n' +
				'在后面学习对象这一章时，我们会学习字符串的一些常用方法，这里先掌握字符串的基本使用即可。\n' +
				'\n' +
				'## 5，`undefined`和`null`\n' +
				'\n' +
				'在JavaScript中，声明但是没有赋值的变量都是`undefined`：\n' +
				'\n' +
				'```javascript\n' +
				'let a;\n' +
				'console.log(a);\n' +
				'```\n' +
				'\n' +
				'![image-20220531205030892](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220531205030892.png)\n' +
				'\n' +
				'`undefined`表示**未定义**，表示这个变量声明了，但是还没有给它定义值，可以说**它是所有变量的默认值**。\n' +
				'\n' +
				'同样地还有`null`，它表示**缺少的标识**，指示变量**未指向任何对象**。\n' +
				'\n' +
				'在使用上，`undefined`和`null`非常类似，但是实质上是不同的，现阶段了解即可，在后面学习了对象之后，大家可以自行在MDN上面查阅两者的区别。\n' +
				'\n' +
				'## 6，算术运算符\n' +
				'\n' +
				'算术运算符主要是在JavaScript中，对数值进行运算的符号。\n' +
				'\n' +
				'### (1) 四则运算和取余\n' +
				'\n' +
				'四则运算，即加减乘除，是我们小学阶段就已经学习过的运算方式。在JavaScript中，用`+`、`-`、`*`和`/`符号分别表示加、减、乘和除，用`%`表示取余。我们先来试一下：\n' +
				'\n' +
				'```javascript\n' +
				'let a = 1 + 1; // 加\n' +
				'let b = 10 - 5; // 减\n' +
				'let c = 3 * 5; // 乘\n' +
				'let d = 10 / 5; // 除\n' +
				'let e = 29 % 3; // 取余\n' +
				'// 变量之间也可以进行运算\n' +
				'let f = a * b;\n' +
				'\n' +
				'console.log(a);\n' +
				'console.log(b);\n' +
				'console.log(c);\n' +
				'console.log(d);\n' +
				'console.log(e);\n' +
				'console.log(f);\n' +
				'```\n' +
				'\n' +
				'![image-20220531210044809](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220531210044809.png)\n' +
				'\n' +
				'可见运算符的使用是非常简单的，也非常符合人的语义。除了直接的数值进行运算之外，变量也可以用于运算。\n' +
				'\n' +
				'这里说一下取余运算，即为取两数除的余数，上述29除以3等于9余2，因此29对3取余则为2。\n' +
				'\n' +
				'运算的优先级也和我们的生活常识是一样的，先乘除后加减。**当然也可以用括号改变优先级，括号中的优先级是最高的，多层括号时最内层优先级最高：**\n' +
				'\n' +
				'```javascript\n' +
				'let a = 2 * 7 + 8;\n' +
				'let b = 2 * (7 + 8);\n' +
				'\n' +
				'console.log(a);\n' +
				'console.log(b);\n' +
				'```\n' +
				'\n' +
				'![image-20220531210215461](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220531210215461.png)\n' +
				'\n' +
				'可见四则运算中可以多个数进行计算，是从左到右的，括号可以改变优先级。\n' +
				'\n' +
				'### (2) 自增、自减和取反\n' +
				'\n' +
				'取反非常简单，就是取一个数的相反数，在数值/变量前面加上`-`即可：\n' +
				'\n' +
				'```javascript\n' +
				'let a = -3;\n' +
				'let b = -a;\n' +
				'\n' +
				'console.log(a);\n' +
				'console.log(b);\n' +
				'```\n' +
				'\n' +
				'![image-20220531210414096](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220531210414096.png)\n' +
				'\n' +
				'然后就是自加、自减，分别用`++`和`--`表示，**表示让某个变量自己加一、减一。**\n' +
				'\n' +
				'先来看一个例子：\n' +
				'\n' +
				'```javascript\n' +
				'let a = 10;\n' +
				'a--;\n' +
				'console.log(a);\n' +
				'\n' +
				'let b = 10;\n' +
				'b++;\n' +
				'console.log(b);\n' +
				'```\n' +
				'\n' +
				'![image-20220531210517266](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220531210517266.png)\n' +
				'\n' +
				'可见在一个变量后面写上`++`即可让这个变量的值自己加一，`--`就是自己减一。\n' +
				'\n' +
				'当然自加和自减事实上分为**前自加/减**和**后自加/减**，上述是后自加/减。\n' +
				'\n' +
				'我们再来看一个例子：\n' +
				'\n' +
				'```javascript\n' +
				'let a = 10;\n' +
				'console.log(a++);\n' +
				'// 再次输出a\n' +
				'console.log(a);\n' +
				'\n' +
				'let b = 10;\n' +
				'console.log(++b);\n' +
				'```\n' +
				'\n' +
				'![image-20220531210624228](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220531210624228.png)\n' +
				'\n' +
				'可见第一次打印的是`a++`，因为`++`写在`a`后面，因此是**后自加**，这时就会**先取其值再自加**。\n' +
				'\n' +
				'而后面打印的是`++b`，`++`位于变量`b`前面，因此是**前自加**，这时会**先自加再取其值**。\n' +
				'\n' +
				'自减运算符也同理。\n' +
				'\n' +
				'好了，再来看一个复杂一点的例子：\n' +
				'\n' +
				'```javascript\n' +
				'let a = 10;\n' +
				'let b = a++ + 1;\n' +
				'let c = --b + 2 + a;\n' +
				'\n' +
				'console.log(a);\n' +
				'console.log(b);\n' +
				'console.log(c);\n' +
				'```\n' +
				'\n' +
				'![image-20220531210747791](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220531210747791.png)\n' +
				'\n' +
				'我们一个个看，先是把`a`赋值为`10`，在`b`中让`a`的后自加加上`1`，由于在`b`的计算中，`a`是后自加的，因此**先取`a`的值`10`加上`1`赋值给`b`**，所以`b`是`11`。\n' +
				'\n' +
				'计算出`b`之后，`a`的值才加了`1`，`a`变成`11`。\n' +
				'\n' +
				'然后计算`c`，其中让`b`的前自减加上`2`再加上`a`。由于在`c`的计算中，`b`是前自减的，因此**先让`b`自减再取其值，为`10`**，最后加上`2`和`a`，得到`23`。由于上述`b`的计算`a`进行过自加，所以这里`a`是`11`。\n' +
				'\n' +
				'大家一步步地看是不是清晰很多了呢？\n' +
				'\n' +
				'事实上，自加/自减的顺序看起来确实很绕，但是还是很好理解的，记住**前自加/自减**是**先进行自加/自减再取其值**，**后自加/自减**是**先取其值再自加/自减**。\n' +
				'\n' +
				'> 除了JavaScript，其它编程语言中也有自加/自减运算符，并且前后顺序和JavaScript是一样的，因此在很多学校的C语言考试中，常常利用前后自加/自减来出一些“阴间”题目，不过大家在这里如果能够理解两种次序的区别，对付其余的编程语言中的这些情况也是绰绰有余的，如果不理解就还是多进行实操。\n' +
				'\n' +
				'### (3) 赋值\n' +
				'\n' +
				'事实上，我们在之前给变量定义值的过程，就是**赋值**。不过在此还是再提一下。\n' +
				'\n' +
				'赋值的运算符是`=`，赋值操作是**从右到左**的。\n' +
				'\n' +
				'我们上面最简单的`let a = 10;`，就是把`10`赋值给变量`a`。\n' +
				'\n' +
				'在日常生活中，`=`这个符号更多的是一个判断，表示两者相等，不过在JavaScript以及其它编程语言中，`=`符号更多表示一种动作，也就是赋值这种动作。这一点大家需要区分开来。\n' +
				'\n' +
				'我们来看几个例子：\n' +
				'\n' +
				'```javascript\n' +
				'let a = 10;\n' +
				'a = 11;\n' +
				'\n' +
				'console.log(a);\n' +
				'```\n' +
				'\n' +
				'![image-20220531211042174](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220531211042174.png)\n' +
				'\n' +
				'这个很简单，变量被声明之后可以通过重新赋值的方式进行修改，注意声明变量和修改变量的形式不同，前者是`let 变量名 = 值`，表示**创建一个对象**。后者是`变量名 = 值`，表示**对现有的变量进行修改**。\n' +
				'\n' +
				'当然，`const`定义的就是常量，是不能再次修改的。\n' +
				'\n' +
				'```javascript\n' +
				'let a = 1;\n' +
				'let b = 2;\n' +
				'// 声明变量c和d但是不赋值\n' +
				'let c, d;\n' +
				'// 把a + b的值同时赋给c和d\n' +
				'c = d = a + b;\n' +
				'\n' +
				'console.log(c);\n' +
				'console.log(d);\n' +
				'```\n' +
				'\n' +
				'![image-20220531211302100](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220531211302100.png)\n' +
				'\n' +
				'可见我们可以一次性声明多个变量，但是不赋值。还可以通过“连等”的方式，一次性给多个变量赋值，上面讲过赋值是从右到左的，因此上述`c = d = a + b`，表示先把`a + b`的值赋值给`d`，再把`d`的值赋值给`c`。大家一定要知道这个次序。\n' +
				'\n' +
				'再来看一个：\n' +
				'\n' +
				'```javascript\n' +
				'let a = 1;\n' +
				'a = a + 1;\n' +
				'console.log(a);\n' +
				'```\n' +
				'\n' +
				'![image-20220531211349332](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220531211349332.png)\n' +
				'\n' +
				'看到`a = a + 1`，大家第一次可能有点困惑，不知道这是什么操作。事实上这样的操作很常见，是**变量对自己的一个运算**。\n' +
				'\n' +
				'`a`的值是`1`，然后进行`a = a + 1`，说人话就是先把`a + 1`得到`2`，然后把这个值赋值给`a`，因此`a`最后是`2`。\n' +
				'\n' +
				'这也**正体现了我们的赋值操作是从右到左的**。\n' +
				'\n' +
				'有同学发现这个和我们自加很像，事实上上述写法也可以达到自加的效果。\n' +
				'\n' +
				'只不过自加只能自加`1`，而对自己进行运算，不仅可以自加/减别的数值，还可以进行自乘、除、取余操作。\n' +
				'\n' +
				'```javascript\n' +
				'let a = 1;\n' +
				'// 把a + 2然后赋给a\n' +
				'a = a + 2;\n' +
				'console.log(a);\n' +
				'\n' +
				'let b = 3;\n' +
				'// 把b × 10然后赋给b\n' +
				'b = b * 10;\n' +
				'console.log(b);\n' +
				'```\n' +
				'\n' +
				'![image-20220531211522846](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220531211522846.png)\n' +
				'\n' +
				'这样的操作可以简写如下：\n' +
				'\n' +
				'```javascript\n' +
				'let a = 1;\n' +
				'a += 2; // 等效于a = a + 2\n' +
				'console.log(a);\n' +
				'\n' +
				'let b = 3;\n' +
				'b *= 10; // 等效于b = b * 10\n' +
				'console.log(b);\n' +
				'```\n' +
				'\n' +
				'![image-20220531211619387](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220531211619387.png)\n' +
				'\n' +
				'算术运算符基本上就这些了，事实上还是很简单的，我们总结如下：\n' +
				'\n' +
				'| 运算符 | 名称 |      示例      |   功能   |\n' +
				'| :----: | :--: | :------------: | :------: |\n' +
				'|  `+`   |  加  |    `x + y`     | 两者相加 |\n' +
				'|  `-`   |  减  |    `x - y`     | 两者相减 |\n' +
				'|  `*`   |  乘  |    `x * y`     | 两者相乘 |\n' +
				'|  `/`   |  除  |    `x / y`     | 两者相除 |\n' +
				'|  `%`   | 取余 |    `x % y`     | 两者取余 |\n' +
				'|  `++`  | 自加 | `x++`或者`++x` |  自加1   |\n' +
				'|  `--`  | 自减 | `x--`或者`--x` |  自减1   |\n' +
				'|  `-`   | 取反 |    `y = -x`    | 取相反数 |\n' +
				'\n' +
				'## 7，关系运算符\n' +
				'\n' +
				'除了计算与赋值，在开发中，对变量的比较也是很常见的。关系运算符就是用于比较两个变量用的运算符，**关系运算符运算得到的结果是一个布尔型值**。\n' +
				'\n' +
				'我们来看以下例子：\n' +
				'\n' +
				'```javascript\n' +
				'let a = 1;\n' +
				'let b = 2;\n' +
				'// 判断a是否大于b，是为真，否则为假\n' +
				'console.log(a > b);\n' +
				'// 判断a是否小于b，是为真，否则为假\n' +
				'console.log(a < b);\n' +
				'// 判断a是否大于等于b，是为真，否则为假\n' +
				'console.log(a >= b);\n' +
				'// 判断a是否小于等于b，是为真，否则为假\n' +
				'console.log(a <= b);\n' +
				'// 判断a是否等于b，是为真，否则为假\n' +
				'console.log(a == b);\n' +
				'// 判断a是否全等于b，是为真，否则为假\n' +
				'console.log(a === b);\n' +
				'// 判断a是否不等于b，是为真，否则为假\n' +
				'console.log(a != b);\n' +
				'// 判断a是否不全等于b，是为真，否则为假\n' +
				'console.log(a !== b);\n' +
				'```\n' +
				'\n' +
				'![image-20220531213146083](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220531213146083.png)\n' +
				'\n' +
				'可见关系运算符的作用就是必将两者，得到的结果就是一个布尔值。在此也可见在JavaScript中，一个等于号`=`表示赋值操作，而两个等于号`==`表示比较是否相等，在其它大多数编程语言中也是这样。\n' +
				'\n' +
				'我们也可以把比较的结果赋值给一个布尔型变量：\n' +
				'\n' +
				'```javascript\n' +
				'let a = 1;\n' +
				'let b = 2;\n' +
				'let bool = a == b;\n' +
				'console.log(bool);\n' +
				'console.log(\'少时诵诗书所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所少时诵诗书所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所少时诵诗书所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所少时诵诗书所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所少时诵诗书所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所所\');\n' +
				'```\n' +
				'\n' +
				'![image-20220531212038372](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220531212038372.png)\n' +
				'\n' +
				'关系运算符是非常简单的，总结如下表：\n' +
				'\n' +
				'| 运算符 |   名称   |   示例    |       功能       |\n' +
				'| :----: | :------: | :-------: | :--------------: |\n' +
				'|  `>`   |   大于   |  `x > y`  |   比较是否大于   |\n' +
				'|  `<`   |   小于   |  `x < y`  |   比较是否小于   |\n' +
				'|  `>=`  | 大于等于 | `x >= y`  | 比较是否大于等于 |\n' +
				'|  `<=`  | 小于等于 | `x <= y`  | 比较是否小于等于 |\n' +
				'|  `==`  |   等于   | `x == y`  |   比较是否相等   |\n' +
				'| `===`  |  全等于  | `x === y` | 比较两者是否全等 |\n' +
				'|  `!=`  |  不等于  | `x != y`  |  比较是否不相等  |\n' +
				'| `!==`  |  不全等  | `x !== y` |  比较是否不全等  |\n' +
				'\n' +
				'除了上述讲解的判断相等之外，还有用`===`判断全等，这个在下面讲解类型转换的部分再来学习。\n' +
				'\n' +
				'## 8，逻辑运算符\n' +
				'\n' +
				'逻辑运算符是一种对布尔型数据进行操作的运算符。\n' +
				'\n' +
				'在我们生活中，常常会对多个条件进行处理判断，例如：今天要是下雨或者起风我就不出门。可见下雨和起风只要有一个条件达成，就不会出门。这其中就蕴含着逻辑运算。\n' +
				'\n' +
				'还是从一个例子开始：\n' +
				'\n' +
				'```javascript\n' +
				'let a = 1;\n' +
				'let b = 2;\n' +
				'let c = 3;\n' +
				'// &&运算符：当左边条件和右边条件全都为真时，则为真\n' +
				'console.log(a + b < 2 && a + b == c);\n' +
				'// ||运算符：当左边条件和右边条件只要有一个为真时，则为真；若全为假，才为假\n' +
				'console.log(a + b < 2 || a + b == c);\n' +
				'// !运算符：取反\n' +
				'console.log(!(a + b == c));\n' +
				'```\n' +
				'\n' +
				'![image-20220531212424558](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/image-20220531212424558.png)\n' +
				'\n' +
				'每一个逻辑运算符都写在上述注释中了，大家可以对照结果看一看。\n' +
				'\n' +
				'上述`&&`被称作**与运算符**，必须两边都达成条件才得到真，而`||`被称作**或运算符**，两边有一个条件达成即得到真。从这两个运算符名字我们就可以推测出两者作用。\n' +
				'\n' +
				'而`!`相当于是对布尔型值取反，类似于上述数值运算中的`-`取相反数。\n' +
				'\n' +
				'逻辑运算符总结如下：\n' +
				'\n' +
				'| 运算符 | 名称 |   示例   |                   功能                   |\n' +
				'| :----: | :--: | :------: | :--------------------------------------: |\n' +
				'|  `&&`  |  与  | `x && y` |       两边都为真才得到真，否则为假       |\n' +
				'|  `||`  |  或  | `x || y` | 两边有一个为真就得到真，全部为假才得到假 |\n' +
				'|  `!`   |  非  |   `!x`   |                布尔值取反                |\n' +
				'## 9，类型转换\n' +
				'\n' +
				'### (1) 隐式转换\n' +
				'\n' +
				'**字符串和数字相加时**，得到的结果为**字符串**，其中在运算时数字会被转换为字符串：\n' +
				'\n' +
				'```javascript\n' +
				'console.log(20 + \'20\'); // 2020\n' +
				'console.log(\'20\' + 20); // 2020\n' +
				'```\n' +
				'而在**字符串与数字做非加运算时**得到的结果是**数字**，运算时字符串会被转换为数字：\n' +
				'```javascript\n' +
				'console.log(\'20\' - \'10\'); // 10\n' +
				'console.log(\'20\' / \'10\'); // 2\n' +
				'console.log(\'20\' * \'10\'); // 200\n' +
				'```\n' +
				'### (2) 强制类型转换\n' +
				'\n' +
				'`parseInt`函数，将整数字符串转换为整数类型：\n' +
				'\n' +
				'```javascript\n' +
				'let str = \'20\';\n' +
				'let num = parseInt(str);\n' +
				'console.log(num);\n' +
				'console.log(typeof num);\n' +
				'```\n' +
				'运行结果如下：\n' +
				'\n' +
				'![](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/2021年9月8日013832-1.png)\n' +
				'\n' +
				'上述`typeof`语句是用于判断数据类型的。\n' +
				'\n' +
				'`parseFloat`函数，将小数字符串转为浮点数，用法和`parseInt`相同。\n' +
				'\n' +
				'如果说对于一个非数值字符串使用强制转换会怎么样呢？很显然会得到`NaN`。\n' +
				'\n' +
				'### (3) 相等和全等\n' +
				'\n' +
				'在JavaScript中使用`==`判断两个对象是否**相等**，而`===`判断两个对象是否**全等**。相等和全等的区别又是什么？\n' +
				'\n' +
				'**相等只判断值是否相同，全等是在相等的基础上还要判断类型是否相同。**\n' +
				'\n' +
				'例如：\n' +
				'\n' +
				'```javascript\n' +
				'let number1 = \'45\';\n' +
				'let number2 = 45;\n' +
				'// 比较两个对象是否相等\n' +
				'console.log(number1 == number2);\n' +
				'// 比较两个对象是否全等\n' +
				'console.log(number1 === number2);\n' +
				'```\n' +
				'\n' +
				'![](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/2021年9月8日013832-2.png)\n' +
				'\n' +
				'在判断相等时，变量会被隐式转换，上述的字符串先被转换成了数字然后判断相等。\n' +
				'\n' +
				'判断全等时，发现两者虽然值一样但是类型不一样，所以两者不全等。\n' +
				'\n' +
				'## 10，变量名规范\n' +
				'\n' +
				'上述例子为了简单起见，变量名全起名为`abcd`这样的单个字母。但是**在实际开发中，这样是不行的**。\n' +
				'\n' +
				'实际项目开发，你的代码可能不仅仅由你一个人完成，因此保持代码的可读性非常重要。别人需要一眼能够看出来你这个变量的作用和意义。因此变量通常命名有**有其实际意义的英文单词**，并**采用小驼峰命名法**。\n' +
				'\n' +
				'和类名的大驼峰命名法不同，小驼峰命名法是组成变量名的**第一个单词小写**，**其余单词首字母大写**，例如：\n' +
				'\n' +
				'- `main`：仅由`main`一个单词组成\n' +
				'- `userName`：由`user`和`name`两个单词组成\n' +
				'- `gameDataService`：由`game`、`data`和`service`三个单词组成\n' +
				'\n' +
				'这就是小驼峰命名法，以后开发变量名建议都采用这样的形式，不会的单词也可以通过[谷歌翻译](https://translate.google.cn/?sl=zh-CN&tl=en&op=translate)查询。\n' +
				'\n' +
				'## 11，总结\n' +
				'\n' +
				'到此，我们已经认识了所有的JavaScript基本数据类型变量以及运算了！可见虽然内容有点多，但是非常好理解。\n' +
				'\n' +
				'事实上，这些变量声明、赋值、乃至一些变量类型，在其它编程语言中也是一样的。\n' +
				'\n---\n' +
				'\n' +
				'我们在此把JavaScript中的基本数据类型总结如下：\n' +
				'\n' +
				'![](https://swsk33-note.oss-cn-shanghai.aliyuncs.com/2022531214507.png)\n');
	},
	updated() {
		if (!this.menuParsed) {
			this.parseTitle();
			this.showCodeTypeAndCopy();
		}
		this.changeCodeStyle(this.isNight);
	}
};
</script>

<style lang="scss">
@font-face {
	font-family: cascadia-code;
	src: url("/src/assets/fonts/CascadiaCode.woff2");
	font-weight: 350;
}

.main-body {
	height: 93vh;

	.menu, .content {
		position: absolute;
		overflow-x: hidden;
		overflow-y: scroll;

		// 设定滚动条整体
		&::-webkit-scrollbar {
			width: 5px;
		}

		// 设定滚动条滑块
		&::-webkit-scrollbar-thumb {
			border-radius: 10px;
			background: rgba(0, 0, 0, 0.2);
		}

		// 设定外层轨道滚动槽
		&::-webkit-scrollbar-track {
			border-radius: 0;
			background: rgba(0, 0, 0, 0.1);
		}
	}

	.menu {
		top: 0;
		left: 0;
		margin: 0;
		padding: 0;
		width: 17vw;
		height: 100%;

		a {
			display: block;
			height: 5vh;
			width: 100%;
			text-decoration: none;
			color: black;
			line-height: 5vh;
			box-sizing: border-box;
			padding-right: 5px;
			white-space: nowrap;
			overflow: hidden;
			text-overflow: ellipsis;
			transition: background-color 1.5s ease-in-out;
			user-select: none;

			&:first-child {
				margin-top: 3%;
			}

			&:active {
				transition: background-color 0s linear;
			}
		}
	}

	.content {
		width: 83vw;
		left: 17vw;
		height: 100%;
		box-sizing: border-box;
		padding: 1vh 8vw;

		// 定义二号标题样式
		h2 {
			border-bottom: 1px solid;
		}

		// 定义代码块样式
		pre {
			position: relative;
			display: block;
			border-radius: 4px;
			box-sizing: content-box;
			padding: 13px 9px;
			overflow-x: auto;

			// 设定语言标识和复制按钮
			.code-language, .copy-button {
				position: absolute;
				top: 0;
				user-select: none;
				color: #858585;
			}

			.code-language {
				right: 42px;
			}

			.copy-button {
				right: 5px;
				cursor: pointer;
			}

			// 设定滚动条整体
			&::-webkit-scrollbar {
				height: 4px;
			}

			// 设定滚动条滑块
			&::-webkit-scrollbar-thumb {
				border-radius: 8px;
				background: rgba(0, 0, 0, 0.2);
			}

			// 设定外层轨道滚动槽
			&::-webkit-scrollbar-track {
				border-radius: 0;
				background: rgba(0, 0, 0, 0.1);
			}
		}

		// 定义文字行内代码样式
		p, h2, h3, h4, h5, h6, li, blockquote {
			code {
				display: inline-block;
				line-height: 125%;
				border-radius: 4px;
				padding-left: 5px;
				padding-right: 5px;
				margin-left: 2px;
				margin-right: 2px;
			}
		}

		// 定义代码大小与字体
		code {
			font-family: cascadia-code, serif;
			font-size: 15px;
		}

		// 定义引用块
		blockquote {
			margin: 0;
			border-radius: 4px;
			padding-top: 2px;
			padding-bottom: 2px;

			p {
				margin: 9px;
				word-wrap: break-word;
			}
		}

		// 图片居中
		p {
			img {
				display: block;
				margin: 0 auto;
				// 设定图片最大宽度
				max-width: 95%;
			}
		}

		// 定义列表
		table {
			margin: 0 auto;
			border-collapse: collapse;

			th, td {
				box-sizing: border-box;
				padding: 10px 15px;
				border: 1px solid gainsboro;
			}

			th {
				color: white;
			}

			tr {
				&:nth-child(even) {
					background-color: #e3e3e3;
				}
			}
		}

		// 定义超链接
		a {
			margin-left: 1.5px;
			margin-right: 1.5px;
		}

		// 定义分割线
		hr {
			border-top: none;
		}
	}
}
</style>
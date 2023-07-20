<template>
	<!-- 主体 -->
	<div class="main-body" v-loading="!loadingDone" :element-loading-text="loadingText">
		<!-- 正文内容 -->
		<div class="content" ref="content" @click="themeStore.setMenuShow(false)"></div>
		<!-- 菜单 -->
		<div class="menu" v-show="themeStore.menuShow">
			<el-tooltip placement="right" v-for="item in menuItems" :key="item.text" :content="item.text">
				<a :style="{paddingLeft: getItemIndentation(item.indentation)}" :href="item.anchor">{{ item.text }}</a>
			</el-tooltip>
		</div>
		<!-- 找不到文章 -->
		<div class="not-found" v-if="isArticleNotFound">
			<div class="text">找不到文章！╮(╯﹏╰）╭</div>
		</div>
	</div>
</template>

<script setup>
import { marked } from 'marked';
import { REQUEST_METHOD, sendRequest } from '../../../utils/request';
import { joinPath } from '../../../utils/file-path';
import { REQUEST_PREFIX } from '../../../param/request-prefix';
import { MESSAGE_TYPE, showNotification } from '../../../utils/message';
import ClipBoard from 'clipboard';
import renderMathInElement from 'katex/dist/contrib/auto-render';
import { onBeforeMount, onUpdated, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { markedHighlight } from 'marked-highlight';
import hljs from 'highlight.js';

const route = useRoute();

const content = ref(null);

// pinia
import { useArticlePageThemeStore } from '../../../store/article-page-theme';

const themeStore = useArticlePageThemeStore();

// marked.js配置高亮
marked.use(markedHighlight({
	langPrefix: 'hljs language-',
	highlight(code, lang) {
		const language = hljs.getLanguage(lang) ? lang : 'plaintext';
		return hljs.highlight(code, { language }).value;
	}
}));

// marked.js其余选项
marked.setOptions({
	headerIds: false,
	mangle: false
});

/**
 * 文章的文本节点
 */
let textDom;
/**
 * 获取到的文章对象
 */
let articleObject;

const loadingDone = ref(false);
const loadingText = ref('正在拉取文章内容...');
const menuItems = ref([]);
const isArticleNotFound = ref(false);

/**
 * 获得目录项的缩进长度，css值形式
 * @param indentationLevel 该项的缩进级别
 */
function getItemIndentation(indentationLevel) {
	return 'calc(8% + ' + indentationLevel * 7 + 'px)';
}

/**
 * 初始化方法-解析标题生成目录树
 * @param contentNode {HTMLBodyElement} 内容所在节点
 */
function parseTitle(contentNode) {
	const doms = contentNode.children;
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
			menuItems.value.push({
				text: dom.innerText,
				level: level,
				anchor: '#' + dom.id
			});
			index++;
		}
	}
	// 根据最大标题设定缩进级别
	for (let item of menuItems.value) {
		item.indentation = item.level - maxLevel;
	}
	themeStore.menuParsed = true;
	// 手机端渲染完成隐藏菜单
	themeStore.setMenuShow(!themeStore.isMobile);
}

/**
 * 初始化方法-批量设置超链接为新标签页打开
 * @param contentNode {HTMLBodyElement} 内容节点
 */
function setLink(contentNode) {
	// 找出所有a标签
	const doms = contentNode.querySelectorAll('a');
	for (let dom of doms) {
		// 如果href属性为#开头，说明是锚点，设定为当前页
		if (dom.getAttribute('href').startsWith('#')) {
			dom.setAttribute('target', '_self');
			continue;
		}
		// 设置a标签为新标签页打开
		dom.setAttribute('target', '_blank');
	}
}

/**
 * 初始化方法-处理所有相对路径的图片
 * @param contentNode {HTMLBodyElement} 内容节点
 */
function alterRelativePathImage(contentNode) {
	// 找出所有img标签
	const doms = contentNode.querySelectorAll('img');
	for (let dom of doms) {
		let imagePath = dom.getAttribute('src');
		// 对相对路径的图片进行路径替换
		if (!imagePath.startsWith('http')) {
			dom.setAttribute('src', REQUEST_PREFIX.ANTHOLOGY + 'get-image/id/' + articleObject.anthology.id + '?path=' + joinPath(imagePath, articleObject.filePath));
		}
	}
}

/**
 * 初始化方法-在代码块中添加代码语言名和复制
 * @param contentNode {HTMLBodyElement} 内容节点
 */
function showCodeTypeAndCopy(contentNode) {
	const pres = contentNode.querySelectorAll('pre');
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
		showNotification('成功', '复制成功！');
		// 清除文本选中状态
		e.clearSelection();
	});
	clipBoard.on('error', (e) => {
		showNotification('失败', '复制失败！请检查浏览器权限！', MESSAGE_TYPE.error);
		// 清除文本选中状态
		e.clearSelection();
	});
}

/**
 * 初始化方法-渲染公式
 * @param contentNode {HTMLBodyElement} 内容节点
 */
function renderKatex(contentNode) {
	renderMathInElement(contentNode, {
		delimiters: [
			{ left: '$$', right: '$$', display: true },
			{ left: '$', right: '$', display: false }
		],
		strict: false,
		throwOnError: false
	});
}

/**
 * 改变代码样式
 * @param isNight 是否改为夜晚样式
 */
function changeCodeStyle(isNight) {
	const preDoms = content.value.querySelectorAll('pre');
	for (let item of preDoms) {
		if (isNight) {
			item.className = 'pre-night';
		} else {
			item.className = 'pre-day';
		}
	}
}

// 监听白天/夜晚模式的变化
watch(() => themeStore.isNight, () => {
	changeCodeStyle(themeStore.isNight);
});

/**
 * 监听是否解析文档节点完成
 */
const parseWatcher = watch(() => themeStore.contentParsed, (newValue) => {
	// 解析文档节点完成后，将节点添加到主体中并结束监听
	if (newValue) {
		content.value.innerHTML = textDom.innerHTML;
		loadingDone.value = true;
		parseWatcher();
	}
});

onBeforeMount(async () => {
	// 拉取文本内容
	const getText = await sendRequest(REQUEST_PREFIX.ARTICLE + 'get/' + route.params.id, REQUEST_METHOD.GET);
	if (getText === undefined || !getText.success) {
		isArticleNotFound.value = true;
		loadingDone.value = true;
		return;
	}
	// 赋值给文章对象
	articleObject = getText.data;
	// 预处理文本
	loadingText.value = '渲染中...';
	const htmlText = marked(articleObject.content);
	// 解析为Dom节点
	const parser = new DOMParser();
	textDom = parser.parseFromString(htmlText, 'text/html').querySelector('body');
	// 执行预处理
	parseTitle(textDom);
	showCodeTypeAndCopy(textDom);
	setLink(textDom);
	alterRelativePathImage(textDom);
	renderKatex(textDom);
	// 设定渲染完成
	themeStore.contentParsed = true;
});

onUpdated(() => {
	// 根据白天或者夜晚模式改变代码样式
	changeCodeStyle(themeStore.isNight);
});
</script>

<style lang="scss">
@font-face {
	font-family: cascadia-code;
	src: url("/src/assets/fonts/CascadiaCode.woff2");
	font-weight: 350;
}

.main-body {
	height: 93vh;

	// 设定所有滚动条
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

	// 左侧目录
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

	// 正文内容
	.content {
		width: 83vw;
		left: 17vw;
		height: 100%;
		box-sizing: border-box;
		padding: 1vh 8vw 5vh 8vw;

		// 定义一号标题样式
		h1 {
			border-bottom: 1px solid;
		}

		// 定义二号标题样式
		h2 {
			border-bottom: 1px solid;
		}

		// 定义代码块的滚动条
		pre, pre code {
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

		// 定义代码块样式
		pre {
			position: relative;
			display: block;
			border-radius: 4px;
			box-sizing: content-box;
			padding: 15px 9px 5px;
			overflow-x: scroll;

			code {
				background-color: #FFFFFF00;
				padding: 0 0 5px;
			}

			// 设定语言标识和复制按钮
			.code-language, .copy-button {
				position: absolute;
				user-select: none;
				color: #858585;
				top: 0;
			}

			.code-language {
				right: 42px;
			}

			.copy-button {
				right: 5px;
				cursor: pointer;
			}
		}

		// 定义文字行内代码样式
		p, h1, h2, h3, h4, h5, h6, li, blockquote {
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

		// 调整一号标题代码样式
		h1 {
			code {
				line-height: 200%;
			}
		}

		// 调整二号标题代码样式
		h2 {
			code {
				line-height: 180%;
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
				max-width: 100%;
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

	.not-found {
		position: relative;
		top: 30%;

		.text {
			text-align: center;
			padding: 3vw;
			font-size: 48px;
		}
	}
}
</style>
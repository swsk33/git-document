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
import { REQUEST_METHOD, sendRequest } from '../../../utils/request.js';

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
	async mounted() {
		// 初始化文本内容
		const getText = await sendRequest('/api/article/get/' + this.$route.params.id, REQUEST_METHOD.GET);
		this.text = marked(getText.data.content);
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

		// 定义一号标题样式
		h1 {
			border-bottom: 1px solid;
		}

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
}
</style>
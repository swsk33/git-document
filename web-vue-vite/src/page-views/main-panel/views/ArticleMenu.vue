<template>
	<div class="anthology-menu">
		<div class="path">
			<div class="head">
				<div class="text">当前位置：</div>
				<el-button class="last" type="primary" size="small" :icon="icons.top" @click="goToLast" circle/>
			</div>
			<ul class="menu-tree">
				<li class="root" @click="depth = -1">/&ensp;</li>
				<li v-for="(item, index) in menuTree">
					<div class="arrow" v-if="index !== 0">&ensp;&gt;&ensp;</div>
					<div class="name" @click="depth = index">{{ item }}</div>
				</li>
			</ul>
		</div>
		<ul class="menu-content">
			<li class="directory" v-for="item in currentPathHandler.directories" @click="enterDirectory(item.name)">
				<Folder class="icon"/>
				<div class="text">{{ item.name }}</div>
			</li>
			<li class="article" v-for="item in currentPathHandler.articles" @click="enterArticle(item.id)">
				<Document class="icon"/>
				<div class="text">{{ item.name }}</div>
			</li>
		</ul>
		<div class="empty-text" v-if="isEmpty">该文集为空！</div>
	</div>
</template>

<script>
import { sendRequest, REQUEST_METHOD } from '../../../utils/request.js';
import { ElNotification } from 'element-plus';
import { Folder, Document, Top } from '@element-plus/icons-vue';
import { shallowRef } from 'vue';

export default {
	components: {
		Folder,
		Document
	},
	data() {
		return {
			icons: {
				top: shallowRef(Top)
			},
			total: {
				directories: [],
				articles: []
			},
			currentPath: '/',
			/**
			 * 目录深度，-1代表当前位于根目录
			 */
			depth: -1
		};
	},
	computed: {
		/**
		 * 用于设定或者获取当前目录
		 */
		currentPathHandler: {
			/**
			 * 获取当前路径下的目录或者文件列表
			 * @returns {{directories: [], articles: []}} 目录列表与文件列表
			 */
			get() {
				let pointer = this.total;
				let paths = this.currentPath.split('/');
				for (let path of paths) {
					if (path === '') {
						continue;
					}
					for (let dir of pointer.directories) {
						if (dir.name === path) {
							pointer = dir;
						}
					}
				}
				return pointer;
			},
			/**
			 * 设定当前路径
			 * @param value 设定路径
			 */
			set(value) {
				this.currentPath = value;
			}
		},
		/**
		 * 顶栏目录树导航操作
		 */
		menuTree: {
			/**
			 * 获取当前目录树
			 * @returns {*[]} 数组形式的当前路径
			 */
			get() {
				let result = [];
				let paths = this.currentPath.split('/');
				for (let item of paths) {
					if (item === '') {
						continue;
					}
					result.push(item);
				}
				return result;
			},
			/**
			 * 设定当前目录树位置
			 * @param value 传入一个数值表示目录深度，表示跳转到数组形式路径中的哪一级
			 */
			set(value) {
				if (value === -1) {
					this.currentPathHandler = '/';
					return;
				}
				let path = '';
				let paths = this.menuTree;
				for (let i = 0; i <= value; i++) {
					path += '/' + paths[i];
				}
				this.currentPathHandler = path;
			}
		},
		/**
		 * 控制空仓库提示字的显示
		 */
		isEmpty() {
			return this.total.directories.length === 0 && this.total.articles.length === 0;
		}
	},
	watch: {
		depth() {
			this.menuTree = this.depth;
		}
	},
	methods: {
		/**
		 * 进入当前目录下的一个目录
		 * @param dir 要进入的目录名
		 */
		enterDirectory(dir) {
			let path = this.currentPath;
			if (!path.endsWith('/')) {
				path += '/';
			}
			path += dir;
			this.currentPathHandler = path;
			// 进入一层，深度+1
			this.depth++;
		},
		/**
		 * 进入文章
		 * @param id 文章id
		 */
		enterArticle(id) {
			window.open('/article/' + id);
		},
		/**
		 * 返回上一级目录
		 */
		goToLast() {
			if (this.depth === -1) {
				this.$router.push('/interior-anthology');
			}
			this.depth--;
		}
	},
	async mounted() {
		const response = await sendRequest('/api/article/get-article-list/' + this.$route.params.id, REQUEST_METHOD.GET);
		if (!response.success) {
			ElNotification({
				title: '错误',
				message: '获取文章列表失败！请联系后端开发者！',
				type: 'error',
				duration: 1000
			});
			return;
		}
		this.total = response.data;
	}
};
</script>

<style lang="scss" scoped>
.anthology-menu {
	.path {
		width: 98%;
		margin-left: 1%;
		margin-top: 1vh;
		user-select: none;

		.head {
			padding-left: 1%;
			height: 3.5vh;
			display: flex;
			align-items: center;

			.text {
				line-height: 3.5vh;
			}

			.last {
				position: absolute;
				right: 2%;
			}
		}

		.menu-tree {
			width: 100%;
			margin: 0;
			padding: 0 0 0 1%;
			box-sizing: border-box;
			height: 4.5vh;
			display: flex;
			justify-content: flex-start;
			align-items: center;
			white-space: nowrap;
			overflow-x: auto;
			overflow-y: hidden;

			// 设定滚动条整体
			&::-webkit-scrollbar {
				height: 5px;
			}

			// 设定滚动条滑块
			&::-webkit-scrollbar-thumb {
				border-radius: 10px;
				background: rgba(0, 0, 0, 0.2);
			}

			// 设定外层轨道滚动槽
			&::-webkit-scrollbar-track {
				border-radius: 5px;
				background: rgba(0, 0, 0, 0.1);
			}

			li {
				display: flex;
				list-style: none;
				cursor: pointer;

				.name {
					&:hover {
						color: #2f3aff;
					}
				}
			}

			.root {
				&:hover {
					color: #ff4fdf;
				}
			}
		}
	}

	.menu-content {
		position: relative;
		left: 2%;
		padding: 0;
		width: 96%;
		margin: 1vh 0 0;

		li {
			display: flex;
			align-items: center;
			border-bottom: #d0d0d0 1px solid;
			height: 6vh;
			user-select: none;
			cursor: pointer;

			&:first-child {
				border-top: #d0d0d0 1px solid;
			}

			.icon {
				position: relative;
				margin-left: 1.2%;
				height: 4.5vh;
			}

			.text {
				position: relative;
				margin-left: 1%;
				width: 85%;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
			}
		}

		.directory {
			&:hover {
				color: #7462ff;
			}
		}

		.article {
			&:hover {
				color: #0e8151;
			}
		}
	}

	.empty-text {
		position: relative;
		font-size: 32px;
		color: gray;
		text-align: center;
		top: 15%;
	}
}
</style>
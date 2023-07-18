<template>
	<div class="anthology-menu" v-loading="!loadingDone" element-loading-text="正在拉取文章目录...">
		<div class="path">
			<div class="head">
				<div class="text">当前位置：</div>
				<el-button class="last" type="primary" @click="goToLast">上一级</el-button>
				<el-button class="committer" type="success" plain @click="router.push('/committer/' + route.params.id)">贡献者</el-button>
			</div>
			<ul class="menu-tree">
				<li class="root" @click="depth = -1">/root&ensp;&gt&ensp;</li>
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
		<div class="empty-text" v-if="isEmpty">(o°ω°o)该文集为空！</div>
	</div>
</template>

<script setup>
import { sendRequest, REQUEST_METHOD } from '../../../utils/request.js';
import { ElNotification } from 'element-plus';
import { Folder, Document, Top } from '@element-plus/icons-vue';
import { computed, onBeforeMount, reactive, ref, shallowRef, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { REQUEST_PREFIX } from '../../../param/request-prefix';

const router = useRouter();
const route = useRoute();

// 响应式变量
const loadingDone = ref(false);
const icons = reactive({
	top: shallowRef(Top)
});
const total = reactive({
	directories: [],
	articles: []
});
const currentPath = ref('/');
// 目录深度，-1代表当前位于根目录
const depth = ref(-1);

// 计算属性

/**
 * 用于设定或者获取当前目录
 */
const currentPathHandler = computed({
	/**
	 * 获取当前路径下的目录或者文件列表
	 * @returns {{directories: [], articles: []}} 目录列表与文件列表
	 */
	get() {
		let pointer = total;
		let paths = currentPath.value.split('/');
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
	 * @param {string} value 设定路径
	 */
	set(value) {
		currentPath.value = value;
	}
});

/**
 * 顶栏目录树导航操作
 */
const menuTree = computed({
	/**
	 * 获取当前目录树
	 * @returns {*[]} 数组形式的当前路径
	 */
	get() {
		let result = [];
		let paths = currentPath.value.split('/');
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
	 * @param {number} value 传入一个数值表示目录深度，表示跳转到数组形式路径中的哪一级
	 */
	set(value) {
		if (value === -1) {
			currentPathHandler.value = '/';
			return;
		}
		let path = '';
		let paths = menuTree.value;
		for (let i = 0; i <= value; i++) {
			path += '/' + paths[i];
		}
		currentPathHandler.value = path;
	}
});

/**
 * 控制空仓库提示字的显示
 */
const isEmpty = computed(() => {
	return total.directories.length === 0 && total.articles.length === 0;
});

// 监听
watch(depth, () => {
	menuTree.value = depth.value;
});

// 自定义方法

/**
 * 进入当前目录下的一个目录
 * @param dir 要进入的目录名
 */
function enterDirectory(dir) {
	let path = currentPath.value;
	if (!path.endsWith('/')) {
		path += '/';
	}
	path += dir;
	currentPathHandler.value = path;
	// 进入一层，深度+1
	depth.value++;
}

/**
 * 进入文章
 * @param id 文章id
 */
function enterArticle(id) {
	window.open('/article/' + id);
}

/**
 * 返回上一级目录
 */
function goToLast() {
	if (depth.value === -1) {
		router.push('/interior-anthology');
	}
	depth.value--;
}

onBeforeMount(async () => {
	const response = await sendRequest(REQUEST_PREFIX.ARTICLE + 'get-article-list/' + route.params.id, REQUEST_METHOD.GET);
	loadingDone.value = true;
	if (!response.success) {
		ElNotification({
			title: '错误',
			message: '获取文章列表失败！请联系后端开发者！',
			type: 'error',
			duration: 1000
		});
		return;
	}
	total.directories = response.data.directories;
	total.articles = response.data.articles;
});
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
			justify-content: end;

			.text {
				position: absolute;
				left: 2%;
				line-height: 3.5vh;
			}

			.last, .committer {
				position: relative;
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
				font-size: 18px;
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
		font-size: 48px;
		color: gray;
		text-align: center;
		top: 25%;
	}
}
</style>
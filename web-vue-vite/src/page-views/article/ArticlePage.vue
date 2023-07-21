<template>
	<div :class="{page: true, 'page-night': themeStore.isNight, 'page-pink': themeStore.pageColor.pink, 'page-blue': themeStore.pageColor.blue, 'page-green': themeStore.pageColor.green, 'page-orange': themeStore.pageColor.orange, 'page-gray': themeStore.pageColor.gray}">
		<TopBar ref="topBar"></TopBar>
		<MainBody></MainBody>
	</div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';

// 引入组件
import TopBar from './components/TopBar.vue';
import MainBody from './components/MainBody.vue';

const topBar = ref(null);

// pinia
import { useMetaDataStore } from '../../store/meta-data';
import { useArticlePageThemeStore } from '../../store/article-page-theme';

const metaStore = useMetaDataStore();
const themeStore = useArticlePageThemeStore();

// 监听器
watch(() => metaStore.organizationName, () => {
	metaStore.setTitle('GitDocument - 文档阅读');
}, {
	immediate: true
});

onMounted(() => {
	// 读取本地缓存记录的主题并切换
	themeStore.setPageColor(window.localStorage.getItem('color'));
	// 若没有读取到夜晚模式储存，则读取时间判断是否是晚上
	let getIsNight = window.localStorage.getItem('night');
	if (getIsNight == null) {
		let time = new Date();
		getIsNight = time.getHours() >= 19 || time.getHours() <= 6;
	} else {
		getIsNight = JSON.parse(getIsNight);
	}
	themeStore.isNight = getIsNight;
	topBar.value.night = getIsNight;
	// 读取设备宽度判断是否是移动设备
	if (window.innerWidth <= 768) {
		themeStore.isMobile = true;
	}
});
</script>

<style lang="scss">
// 页面平常样式
.page {
	.top-bar, .main-body {
		position: relative;
	}
}

// 页面夜晚样式
.page-night {
	color: white;
	background-color: #141414;

	.top-bar {
		.tree-container {
			background-color: #0d1117;

			.tree {
				color: white;
				background-color: #0d1117;
			}
		}
	}

	.main-body {
		.menu {
			border-right: 1px solid #ffffff;

			a {
				color: white;
			}
		}

		.content {
			pre {
				// 设定语言标识和复制按钮
				.code-language, .copy-button {
					color: #dadada;
				}
			}

			// 定义列表
			table {
				th {
					color: white;
				}
			}
		}
	}
}

// 手机模式
@media screen and (max-width: 768px) {
	.page {
		.top-bar {
			.show-menu-button {
				display: block;
			}

			.switch-article {
				left: 6.5vh;
			}

			.color-box {
				width: 42%;
				margin-right: 6px;

				.text {
					display: none
				}
			}
		}

		.main-body {
			.menu {
				left: 1.25vh;
				height: 30%;
				width: 55%;
				background-color: white;
				border: 1px gray solid;
				border-radius: 4px;
			}

			.content {
				width: 100vw;
				left: 0;
				padding: 2vh 6vw;

				pre {
					// 设定语言标识和复制按钮
					.code-language, .copy-button {
						font-size: 15px;
					}
				}
			}
		}
	}

	.page-night {
		.main-body {
			.menu {
				background-color: #141414;
			}
		}
	}
}

// 引入代码样式
@import "../../assets/scss/code/github.scss";
@import "../../assets/scss/code/github-dark.scss";
// 引入颜色主题
@import "../../assets/scss/color/pink.scss";
@import "../../assets/scss/color/blue.scss";
@import "../../assets/scss/color/orange.scss";
@import "../../assets/scss/color/green.scss";
@import "../../assets/scss/color/gray.scss";
</style>
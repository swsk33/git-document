<template>
	<div :class="{page: true, 'page-night': isNight, 'page-pink': pageColor.pink, 'page-blue': pageColor.blue, 'page-green': pageColor.green, 'page-orange': pageColor.orange, 'page-gray': pageColor.gray}">
		<top-bar ref="topBar"></top-bar>
		<router-view></router-view>
	</div>
</template>

<script>
// element-plus消息
import { ElTooltip } from 'element-plus';
import { createNamespacedHelpers } from 'vuex';

// 引入组件
import topBar from './components/TopBar.vue';
import { REQUEST_METHOD, sendRequest } from '../../utils/request.js';

// vuex模块
const { mapState: themeState, mapActions: themeActions } = createNamespacedHelpers('articlepagetheme');
const { mapActions: userActions } = createNamespacedHelpers('user');

export default {
	components: {
		'el-tooltip': ElTooltip,
		'top-bar': topBar
	},
	computed: {
		...themeState(['menuShow', 'isNight', 'isMobile', 'pageColor'])
	},
	methods: {
		...themeActions(['setMenuShow', 'setIsNight', 'setIsMobile', 'setPageColor']),
		...userActions(['checkLogin'])
	},
	async mounted() {
		// 若未登录则跳转到登录页
		if (!await this.checkLogin()) {
			location.href = '/login';
			return;
		}
		// 设定标签页标题
		const getOrganization = await sendRequest('/api/config-get/organization', REQUEST_METHOD.GET);
		document.title = getOrganization.data + ' | GitDocument - 文档阅读';
		// 读取本地缓存记录的主题并切换
		this.setPageColor(window.localStorage.getItem('color'));
		// 若没有读取到夜晚模式储存，则读取时间判断是否是晚上
		let getIsNight = window.localStorage.getItem('night');
		if (getIsNight == null) {
			let time = new Date();
			getIsNight = time.getHours() >= 19 || time.getHours() <= 6;
		} else {
			getIsNight = JSON.parse(getIsNight);
		}
		this.setIsNight(getIsNight);
		this.$refs.topBar.night = getIsNight;
		// 读取设备宽度判断是否是移动设备
		if (window.innerWidth <= 768) {
			this.setIsMobile(true);
			this.setMenuShow(!this.isMobile);
		}
	}
};
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
				display: flex;
			}

			.color-box {
				width: 50%;
				margin-right: 10px;
			}
		}

		.main-body {
			.menu {
				left: 1.25vh;
				height: 45%;
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
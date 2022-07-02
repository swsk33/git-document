<template>
	<div class="main-panel">
		<top-bar ref="topBar"></top-bar>
		<side-menu ref="sideMenu" @closeAvatarMenu="closeAvatarMenu"></side-menu>
		<router-view ref="controlPanel" @click="closeAvatarMenu" class="panel-content"/>
	</div>
</template>

<script>
import { createNamespacedHelpers } from 'vuex';

import topBar from './components/TopBar.vue';
import sideMenu from './components/SideMenu.vue';

const { mapGetters: orgGetter } = createNamespacedHelpers('organization');

export default {
	components: {
		'side-menu': sideMenu,
		'top-bar': topBar
	},
	data() {
		return {};
	},
	computed: {
		...orgGetter(['organizationName'])
	},
	methods: {
		/**
		 * 关闭头像菜单
		 */
		closeAvatarMenu() {
			this.$refs.topBar.menuControl(false);
		}
	},
	created() {
		// 设定标题
		document.title = this.organizationName.value + ' | GitDocument';
	}
};
</script>

<style lang="scss" scoped>
.main-panel {
	position: absolute;
	width: 100%;
	height: 100vh;
	background: url("/api/config-get/background") no-repeat center/cover;

	.panel-content {
		position: absolute;
		left: 18vw;
		top: 8vh;
		width: 80vw;
		height: 90vh;
		background-color: rgba(255, 255, 255, 0.45);
		border-radius: 6px;
		overflow-y: scroll;

		// 设定滚动条整体
		&::-webkit-scrollbar {
			width: 5px;
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
	}
}
</style>
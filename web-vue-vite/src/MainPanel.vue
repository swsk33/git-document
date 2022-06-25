<template>
	<div class="main-panel">
		<top-bar ref="topBar"></top-bar>
		<side-menu></side-menu>
		<router-view class="panel-content" />
	</div>
</template>

<script>
import { createNamespacedHelpers } from 'vuex';

import topBar from './components/TopBar.vue';
import sideMenu from './components/SideMenu.vue';

const { mapState: userState, mapActions: userActions } = createNamespacedHelpers('user');

export default {
	components: {
		'side-menu': sideMenu,
		'top-bar': topBar
	},
	data() {
		return {};
	},
	computed: {
		...userState(['userData'])
	},
	methods: {
		...userActions(['checkLogin']),
		/**
		 * 关闭头像菜单
		 */
		closeAvatarMenu() {
			this.$refs.topBar.menuControl(false);
		}
	},
	async mounted() {
		// 若未登录则跳转到登录页
		if (!await this.checkLogin()) {
			location.href = '/login';
			return;
		}
		// 传递信息给子组件
		this.$refs.topBar.nickname = this.userData.nickname;
		this.$refs.topBar.avatar = this.userData.avatar;
		if (this.userData.role.id === 1) {
			this.$refs.topBar.role = '管理员';
		} else {
			this.$refs.topBar.role = '团队成员';
		}
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
		left: 16vw;
		top: 6vh;
		width: 84vw;
		height: 94vh;
	}
}
</style>
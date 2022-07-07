<template>
	<div class="login-page">
		<div class="welcome-text">Welcome Back! 来自{{ organizationName.value }}的成员</div>
		<router-view class="panel"/>
	</div>
</template>

<script>
import { createNamespacedHelpers } from 'vuex';

const { mapGetters: orgGetter } = createNamespacedHelpers('organization');
const { mapGetters: userGetter } = createNamespacedHelpers('user');

export default {
	computed: {
		...orgGetter(['organizationName']),
		...userGetter(['isLogin'])
	},
	async created() {
		// 若用户已登录则访问这个页面时跳转至/
		if (this.isLogin) {
			await this.$router.push('/');
		}
		document.title = this.organizationName.value + ' | GitDocument - 用户登录';
	}
};
</script>

<style lang="scss" scoped>
.login-page {
	position: absolute;
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	align-items: center;
	top: 0;
	left: 0;
	width: 100%;
	height: 100vh;
	background: url("/api/config-get/login-background") no-repeat center/cover;

	.welcome-text {
		position: relative;
		top: 8%;
		font-size: 28px;
		width: 75%;
		text-align: center;
		background: rgba(202, 230, 255, 0.35);
		box-shadow: lightblue 1px 1px 7px;
		padding: 8px;
		border-radius: 8px;
	}

	.panel {
		bottom: 23%;
	}
}

// 手机模式
@media screen and (max-width: 768px) {
	.login-page {
		.welcome-text {
			font-size: 24px;
			width: 80%;
		}

		.panel {
			width: 85%;
			height: 45%;
		}
	}
}
</style>
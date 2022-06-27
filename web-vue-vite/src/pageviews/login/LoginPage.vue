<template>
	<div class="login-page">
		<div class="welcome-text">Welcome Back! 来自{{ organization }}的成员</div>
		<router-view class="panel"/>
	</div>
</template>

<script>
import { sendRequest, REQUEST_METHOD } from '../../utils/request';
import { createNamespacedHelpers } from 'vuex';

const { mapActions: userAction } = createNamespacedHelpers('user');

export default {
	data() {
		return {
			organization: undefined
		};
	},
	methods: {
		...userAction(['checkLogin'])
	},
	async mounted() {
		// 若已登录则跳转到主面板
		if (await this.checkLogin()) {
			location.href = '/';
			return;
		}
		const getOrganization = await sendRequest('/api/config-get/organization', REQUEST_METHOD.GET);
		this.organization = getOrganization.data;
		document.title = this.organization + ' | GitDocument - 用户登录';
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
</style>
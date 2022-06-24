<template>
	<div class="login-page">
		<div class="welcome-text">Welcome Back! 来自{{ organization }}的成员</div>
		<router-view class="panel" @showRegister="toRegister" @showLogin="toLogin"/>
	</div>
</template>

<script>
import { ElNotification } from 'element-plus';
import { sendRequest, REQUEST_METHOD } from '../../utils/request';

export default {
	data() {
		return {
			organization: undefined,
			allowPublic: false
		};
	},
	methods: {
		toRegister() {
			if (!this.allowPublic) {
				ElNotification(
						{
							title: '错误！',
							message: '本站不允许访客注册！请联系管理员！',
							type: 'error',
							duration: 1000
						}
				);
				return;
			}
			this.$router.push('/login/register');
		},
		toLogin() {
			this.$router.push('/login');
		}
	},
	async mounted() {
		const getOrganization = await sendRequest('/api/config-get/organization', REQUEST_METHOD.GET);
		this.organization = getOrganization.data;
		const getAllowPublic = await sendRequest('/api/config-get/allow-public', REQUEST_METHOD.GET);
		this.allowPublic = getAllowPublic.data;
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
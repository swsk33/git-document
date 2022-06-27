<template>
	<div class="login-panel">
		<div class="text">用户登录</div>
		<div class="input-box">
			<el-input size="large" class="text-input" v-model="userData.username" placeholder="用户名或者邮箱" :prefix-icon="icons.avatar"/>
			<el-input size="large" class="text-input" v-model="userData.password" show-password placeholder="密码" :prefix-icon="icons.lock"/>
		</div>
		<div class="button-box">
			<el-button class="button" type="primary" size="large" @click="login(userData)">登录</el-button>
			<el-button class="button" type="success" size="large" @click="toRegister">注册</el-button>
		</div>
		<el-button class="forget-password" type="warning" size="small" @click="forgetPassword">忘记密码</el-button>
	</div>
</template>

<script>
import { createNamespacedHelpers } from 'vuex';
import { shallowRef } from 'vue';

import { Avatar, Lock } from '@element-plus/icons-vue';
import { REQUEST_METHOD, sendRequest } from '../../../utils/request';
import { ElNotification } from 'element-plus';

// vuex
const { mapActions: userActions } = createNamespacedHelpers('user');

export default {
	data() {
		return {
			userData: {
				username: undefined,
				password: undefined
			},
			icons: {
				avatar: shallowRef(Avatar),
				lock: shallowRef(Lock)
			},
			allowPublic: false
		};
	},
	methods: {
		...userActions(['checkLogin']),
		/**
		 * 跳转至注册页视图
		 */
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
		/**
		 * 登录方法
		 */
		async login() {
			let response = await sendRequest('/api/user/login', REQUEST_METHOD.POST, this.userData);
			if (!response.success) {
				ElNotification({
					title: '错误！',
					message: response.message,
					type: 'error',
					duration: 1000
				});
				return;
			}
			ElNotification({
				title: '成功！',
				message: '登录成功！1s后跳转...',
				type: 'success',
				duration: 750
			});
			// 获取用户信息
			await this.checkLogin();
			setTimeout(() => {
				location.href = '/';
			}, 1000);
		},
		/**
		 * 忘记密码
		 */
		forgetPassword() {
			ElNotification({
				title: '提示',
				message: '请联系管理员重置密码！',
				type: 'warning',
				duration: 1000
			});
		}
	},
	async mounted() {
		const getAllowPublic = await sendRequest('/api/config-get/allow-public', REQUEST_METHOD.GET);
		this.allowPublic = getAllowPublic.data;
	}
};
</script>

<style lang="scss" scoped>
.login-panel {
	position: relative;
	display: flex;
	flex-direction: column;
	justify-content: flex-start;
	align-items: center;
	width: 30%;
	height: 50%;
	border-radius: 8px;
	box-shadow: white 1px 1px 7px;
	background: rgba(255, 255, 255, 0.42);

	.text {
		margin-top: 7%;
		position: relative;
		font-size: 32px;
	}

	.input-box {
		position: relative;
		margin-top: 16px;
		width: 100%;
		height: 45%;
		display: flex;
		flex-direction: column;
		align-items: center;

		.text-input {
			width: 75%;
			margin-top: 28px;
		}
	}

	.button-box {
		position: relative;
		width: 75%;
		height: 15%;
		display: flex;
		justify-content: space-evenly;
		align-items: center;
		margin-top: 10px;

		.button {
			width: 72px;
			font-size: 18px;
		}
	}

	.forget-password {
		position: absolute;
		bottom: 8px;
		right: 8px;
	}
}
</style>
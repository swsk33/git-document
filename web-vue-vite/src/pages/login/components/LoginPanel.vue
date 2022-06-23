<template>
	<div class="login-panel">
		<div class="text">用户登录</div>
		<div class="input-box">
			<el-input size="large" class="text-input" v-model="userData.username" placeholder="用户名或者邮箱" :prefix-icon="icons.avatar"/>
			<el-input size="large" class="text-input" v-model="userData.password" show-password placeholder="密码" :prefix-icon="icons.lock"/>
		</div>
		<div class="button-box">
			<el-button class="button" type="primary" size="large">登录</el-button>
			<el-button class="button" type="success" size="large">注册</el-button>
		</div>
	</div>
</template>

<script>
import { createNamespacedHelpers } from 'vuex';
import { shallowRef } from 'vue';

import { Avatar, Lock } from '@element-plus/icons-vue';

// vuex
const { mapState: userState, mapActions: userActions } = createNamespacedHelpers('user');

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
			}
		};
	},
	methods: {
		...userActions(['login', 'checkLogin'])
	}
};
</script>

<style lang="scss" scoped>
.login-panel {
	position: absolute;
	display: flex;
	flex-direction: column;
	justify-content: flex-start;
	align-items: center;
	width: 35%;
	height: 50%;
	border: 1px gray solid;
	background: rgba(255, 255, 255, 0.42);

	.text {
		margin-top: 10%;
		position: relative;
		font-size: 32px;
	}

	.input-box {
		position: relative;
		margin-top: 12px;
		width: 100%;
		height: 45%;
		display: flex;
		flex-direction: column;
		align-items: center;

		.text-input {
			width: 75%;
			margin-top: 24px;
		}
	}

	.button-box {
		position: relative;
		width: 75%;
		height: 15%;
		display: flex;
		justify-content: space-evenly;
		align-items: center;

		.button {
			width: 72px;
			font-size: 18px;
		}
	}
}
</style>
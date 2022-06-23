<template>
	<div class="login-panel">
		<div class="text">用户登录</div>
		<div class="input-box">
			<el-input size="large" class="text-input" v-model="userData.username" placeholder="用户名或者邮箱" :prefix-icon="icons.avatar"/>
			<el-input size="large" class="text-input" v-model="userData.password" show-password placeholder="密码" :prefix-icon="icons.lock"/>
		</div>
		<div class="button-box">
			<el-button class="button" type="primary" size="large">登录</el-button>
			<el-button class="button" type="success" size="large" @click="$emit('showRegister')">注册</el-button>
		</div>
	</div>
</template>

<script>
import { createNamespacedHelpers } from 'vuex';
import { shallowRef } from 'vue';

import { Avatar, Lock } from '@element-plus/icons-vue';

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
}
</style>
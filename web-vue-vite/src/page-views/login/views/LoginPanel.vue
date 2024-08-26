<template>
	<div class="login-panel">
		<div class="text">用户登录</div>
		<div class="input-box">
			<el-input size="large" class="text-input" v-model="userData.username" placeholder="用户名或者邮箱" :prefix-icon="icons.avatar"/>
			<el-input size="large" class="text-input" v-model="userData.password" show-password placeholder="密码" :prefix-icon="icons.lock" @keydown="enterKeyLogin($event)"/>
		</div>
		<div class="button-box">
			<el-button class="button" type="primary" size="large" @click="login(userData)">登录</el-button>
			<el-button class="button" type="success" size="large" @click="toRegister">注册</el-button>
		</div>
		<el-button class="forget-password" type="warning" size="default" @click="router.push('/login/forget-password')">忘记密码</el-button>
	</div>
</template>

<script setup>
import { Avatar, Lock } from '@element-plus/icons-vue';
import { reactive, shallowRef } from 'vue';
import { useRouter } from 'vue-router';
import { MESSAGE_TYPE, showNotification } from '../../../utils/message.js';

const router = useRouter();

// pinia
import { useUserStore } from '../../../store/user.js';
import { useMetaDataStore } from '../../../store/meta-data.js';
import { useUrlPathStore } from '../../../store/url-path.js';
import { userLogin } from '../../../api/user-api.js';

const userStore = useUserStore();
const metaStore = useMetaDataStore();
const urlStore = useUrlPathStore();

/**
 * 登录的用户数据
 */
const userData = reactive({
	username: undefined,
	password: undefined
});

/**
 * 图标
 */
const icons = reactive({
	avatar: shallowRef(Avatar),
	lock: shallowRef(Lock)
});

/**
 * 跳转至注册页视图
 */
function toRegister() {
	if (!metaStore.allowPublic) {
		showNotification('错误', '本站不允许访客注册！请联系管理员！', MESSAGE_TYPE.error);
		return;
	}
	router.push('/login/register');
}

/**
 * 登录方法
 */
async function login() {
	let response = await userLogin(userData);
	if (!response.success) {
		showNotification('错误', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
	// 获取用户信息
	await userStore.checkLogin();
	// 跳转至用户访问的页面
	// 防止跳转到登录页自己
	if (urlStore.path.startsWith('/login')) {
		await router.push('/');
		return;
	}
	await router.push(urlStore.path);
}

/**
 * 在密码输入框按下enter键时也执行登录
 */
function enterKeyLogin(e) {
	if (e.key === 'Enter') {
		login(userData);
	}
}
</script>

<style lang="scss" scoped>
.login-panel {
	.text {
		margin-top: 8%;
		position: relative;
		font-size: 32px;
	}

	.input-box {
		position: relative;
		width: 78%;
		height: 48%;
		margin-top: 3%;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: space-around;

		.text-input {
			height: 18%;
			font-size: 18px;
		}
	}

	.button-box {
		position: relative;
		width: 78%;
		height: 22%;
		display: flex;
		justify-content: space-evenly;
		align-items: center;

		.button {
			font-size: 19px;
			width: 16%;
			height: 42%;
		}
	}

	.forget-password {
		position: absolute;
		bottom: 8px;
		right: 8px;
		font-size: 16px;
	}
}
</style>
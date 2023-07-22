<template>
	<div class="register-panel">
		<div class="text">用户注册</div>
		<div class="input-box">
			<el-input class="text-input" v-model="userData.username" placeholder="用户名" :prefix-icon="icons.avatar"/>
			<el-input class="text-input" v-model="userData.password" show-password placeholder="密码" :prefix-icon="icons.lock"/>
			<el-input class="text-input" v-model="userData.nickname" placeholder="昵称" :prefix-icon="icons.tag"/>
			<el-input class="text-input" v-model="userData.email" placeholder="邮箱" :prefix-icon="icons.post" @keydown="enterKeyRegister($event)"/>
		</div>
		<div class="button-box">
			<el-button class="button" type="primary" size="large" @click="register">注册</el-button>
			<el-button class="button" type="success" size="large" @click="router.push('/login')">返回</el-button>
		</div>
	</div>
</template>

<script setup>
import { reactive, shallowRef } from 'vue';
import { sendRequest, REQUEST_METHOD } from '../../../utils/request';
import { Avatar, Lock, CollectionTag, Postcard } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';
import { REQUEST_PREFIX } from '../../../param/request-prefix';
import { MESSAGE_TYPE, showNotification } from '../../../utils/message';

const router = useRouter();

/**
 * 注册用户数据
 */
const userData = reactive({
	username: undefined,
	password: undefined,
	nickname: undefined,
	email: undefined,
	role: {
		id: 3
	}
});

/**
 * 图标
 */
const icons = reactive({
	avatar: shallowRef(Avatar),
	lock: shallowRef(Lock),
	tag: shallowRef(CollectionTag),
	post: shallowRef(Postcard)
});

/**
 * 注册方法
 */
async function register() {
	const response = await sendRequest(REQUEST_PREFIX.USER + 'register', REQUEST_METHOD.POST, userData);
	if (!response.success) {
		showNotification('失败', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', '注册成功！请登录！');
	await router.push('/login');
}

/**
 * 在邮箱输入框按下enter键时也执行登录
 */
function enterKeyRegister(e) {
	if (e.key === 'Enter') {
		register();
	}
}
</script>

<style lang="scss" scoped>
.register-panel {
	.text {
		margin-top: 6%;
		position: relative;
		font-size: 32px;
	}

	.input-box {
		position: relative;
		margin-top: 3%;
		width: 100%;
		height: 55%;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: space-evenly;

		.text-input {
			width: 75%;
			height: 16%;
			font-size: 18px;
		}
	}

	.button-box {
		position: relative;
		width: 75%;
		height: 20%;
		display: flex;
		justify-content: space-evenly;
		align-items: center;

		.button {
			width: 16%;
			height: 42%;
			font-size: 19px;
		}
	}
}
</style>
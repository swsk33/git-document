<template>
	<div class="register-panel">
		<div class="text">用户注册</div>
		<div class="input-box">
			<el-input class="text-input" v-model="userData.username" placeholder="用户名" :prefix-icon="icons.avatar"/>
			<el-input class="text-input" v-model="userData.password" show-password placeholder="密码" :prefix-icon="icons.lock"/>
			<el-input class="text-input" v-model="userData.nickname" placeholder="昵称" :prefix-icon="icons.tag"/>
			<el-input class="text-input" v-model="userData.email" placeholder="邮箱" :prefix-icon="icons.post"/>
		</div>
		<div class="button-box">
			<el-button class="button" type="primary" size="large">注册</el-button>
			<el-button class="button" type="success" size="large" @click="$emit('showLogin')">返回</el-button>
		</div>
	</div>
</template>

<script>
import { shallowRef } from 'vue';

import { Avatar, Lock, CollectionTag, Postcard } from '@element-plus/icons-vue';

export default {
	data() {
		return {
			userData: {
				username: undefined,
				password: undefined,
				nickname: undefined,
				email: undefined
			},
			icons: {
				avatar: shallowRef(Avatar),
				lock: shallowRef(Lock),
				tag: shallowRef(CollectionTag),
				post: shallowRef(Postcard)
			}
		};
	}
};
</script>

<style lang="scss" scoped>
.register-panel {
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
			margin-top: 10px;
		}
	}

	.button-box {
		position: relative;
		width: 75%;
		height: 10%;
		display: flex;
		justify-content: space-evenly;
		align-items: center;
		margin-top: 42px;

		.button {
			width: 72px;
			font-size: 18px;
		}
	}
}
</style>
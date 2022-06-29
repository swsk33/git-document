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
			<el-button class="button" type="primary" size="large" @click="register">注册</el-button>
			<el-button class="button" type="success" size="large" @click="this.$router.push('/login')">返回</el-button>
		</div>
	</div>
</template>

<script>
import { shallowRef } from 'vue';
import { sendRequest, REQUEST_METHOD } from '../../../utils/request';
import { ElNotification } from 'element-plus';

import { Avatar, Lock, CollectionTag, Postcard } from '@element-plus/icons-vue';

export default {
	data() {
		return {
			userData: {
				username: undefined,
				password: undefined,
				nickname: undefined,
				email: undefined,
				role: {
					id: 2
				}
			},
			icons: {
				avatar: shallowRef(Avatar),
				lock: shallowRef(Lock),
				tag: shallowRef(CollectionTag),
				post: shallowRef(Postcard)
			}
		};
	},
	methods: {
		/**
		 * 注册方法
		 */
		async register() {
			const response = await sendRequest('/api/user/register', REQUEST_METHOD.POST, this.userData);
			if (!response.success) {
				ElNotification(
						{
							title: '错误！',
							message: response.message,
							type: 'error',
							duration: 1000
						}
				);
				return;
			}
			ElNotification(
					{
						title: '成功！',
						message: '注册成功！请登录！',
						type: 'success',
						duration: 1000
					}
			);
			await this.$router.push('/login');
		}
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
		margin-top: 5%;
		width: 100%;
		height: 50%;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: space-evenly;

		.text-input {
			width: 75%;
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
			width: 72px;
			font-size: 18px;
		}
	}
}
</style>
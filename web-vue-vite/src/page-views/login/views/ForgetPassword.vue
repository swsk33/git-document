<template>
	<div class="reset-password-panel">
		<div class="text">密码重置</div>
		<div class="input-box">
			<el-input class="text-input" v-model="resetUser.email" placeholder="用户邮箱" :prefix-icon="icons.message"/>
			<el-input class="text-input" v-model="resetUser.password" show-password placeholder="新密码" :prefix-icon="icons.lock"/>
			<div class="code-box">
				<el-input class="text-input" v-model="code" placeholder="邮箱验证码" :prefix-icon="icons.coin"/>
				<el-button class="send" type="warning" :disabled="!sendCodeEnable" @click="sendCode">{{ sendCodeText }}</el-button>
			</div>
		</div>
		<div class="button-box">
			<el-button class="button" type="primary" size="large" @click="resetPassword">重置</el-button>
			<el-button class="button" type="success" size="large" @click="router.push('/login')">返回</el-button>
		</div>
	</div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { ref, reactive, shallowRef } from 'vue';
import { Lock, Message, Coin } from '@element-plus/icons-vue';
import { MESSAGE_TYPE, showNotification } from '../../../utils/message.js';
import { isEmpty } from '../../../utils/string.js';
import { emailPasswordResetCode } from '../../../api/email-api.js';
import { userResetPassword } from '../../../api/user-api.js';

const router = useRouter();

/**
 * 图标
 */
const icons = reactive({
	lock: shallowRef(Lock),
	message: shallowRef(Message),
	coin: shallowRef(Coin)
});

/**
 * 要重置密码的用户信息
 */
const resetUser = reactive({
	email: undefined,
	password: undefined
});

/**
 * 验证码
 */
const code = ref(undefined);

/**
 * 验证码按钮是否可用
 */
const sendCodeEnable = ref(true);

/**
 * 验证码按钮文字
 */
const sendCodeText = ref('发送验证码');

/**
 * 发送验证码
 */
async function sendCode() {
	// 检验空
	if (isEmpty(resetUser.email)) {
		showNotification('错误', '邮箱不能为空！', MESSAGE_TYPE.error);
		return;
	}
	const response = await emailPasswordResetCode(resetUser.email);
	if (!response.success) {
		showNotification('错误', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
	// 禁用按钮60s
	let time = ref(60);
	sendCodeEnable.value = false;
	sendCodeText.value = time.value + 's';
	const disableTimer = setInterval(() => {
		time.value--;
		sendCodeText.value = time.value + 's';
		if (time.value <= 0) {
			sendCodeEnable.value = true;
			sendCodeText.value = '发送验证码';
			clearInterval(disableTimer);
		}
	}, 1000);
}

/**
 * 发送密码重置请求
 */
async function resetPassword() {
	// 检验空
	if (isEmpty(resetUser.password) || isEmpty(code.value)) {
		showNotification('错误', '新密码或者验证码不能为空！', MESSAGE_TYPE.error);
		return;
	}
	const response = await userResetPassword(resetUser, code.value);
	if (!response.success) {
		showNotification('错误', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
	await router.push('/login');
}
</script>

<style lang="scss" scoped>
.reset-password-panel {
	.text {
		margin-top: 6%;
		position: relative;
		font-size: 32px;
	}

	.input-box {
		position: relative;
		width: 78%;
		height: 48%;
		margin-top: 6%;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: space-around;

		.text-input {
			height: 18%;
			font-size: 18px;
		}

		.code-box {
			display: flex;
			justify-content: space-between;
			align-items: center;
			width: 100%;
			height: 20%;

			.text-input {
				height: 80%;
				width: 50%;
				font-size: 14px;
			}

			.send {
				height: 80%;
				width: 28%;
				font-size: 18px;
			}
		}
	}

	.button-box {
		position: relative;
		width: 75%;
		height: 20%;
		margin-top: 2%;
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
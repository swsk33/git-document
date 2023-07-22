<template>
	<div class="login-page" :style="backgroundStyle">
		<div class="welcome-text">Welcome Back! 来自{{ metaStore.organizationName }}的成员</div>
		<router-view class="panel"/>
	</div>
</template>

<script setup>
import { onBeforeMount, watch, reactive } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// pinia
import { useMetaDataStore } from '../../store/meta-data';
import { useUserStore } from '../../store/user';

const metaStore = useMetaDataStore();
const userStore = useUserStore();

// 设定登录页背景的CSS变量
const backgroundStyle = reactive({
	background: ''
});

// 监听标题是否加载以实时设定标题
watch(() => metaStore.organizationName, () => {
	metaStore.setTitle('GitDocument - 用户登录');
}, {
	immediate: true
});

// 监听登录页背景是否加载以实时设定背景图片
watch(() => metaStore.loginBackground, () => {
	backgroundStyle.background = 'url("' + metaStore.loginBackground + '") no-repeat center/cover';
}, {
	immediate: true
});

onBeforeMount(async () => {
	// 若用户已登录则访问这个页面时跳转至/
	if (await userStore.checkLogin()) {
		await router.push('/');
	}
});
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
		position: relative;
		display: flex;
		bottom: 21%;
		width: 36%;
		height: 53%;
		flex-direction: column;
		justify-content: flex-start;
		align-items: center;
		border-radius: 8px;
		box-shadow: white 1px 1px 7px;
		background: rgba(255, 255, 255, 0.42);
	}
}

// 手机模式
@media screen and (max-width: 768px) {
	.login-page {
		.welcome-text {
			font-size: 24px;
			width: 80%;
		}

		.panel {
			width: 85%;
			height: 45%;
		}
	}
}
</style>
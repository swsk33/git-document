<!-- 根组件，仅挂载页面视图 -->
<template>
	<router-view class="root-page"/>
</template>

<script setup>
import { useUserStore } from './store/user.js';
import { useUrlPathStore } from './store/url-path.js';
import { useMetaDataStore } from './store/meta-data.js';

// pinia
const userStore = useUserStore();
const urlStore = useUrlPathStore();
const metaStore = useMetaDataStore();

import { onBeforeMount } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

onBeforeMount(async () => {
	// 初始化后端配置数据
	await metaStore.getAllMeta();
	// 跳转之前，获取第一次用户访问路径
	urlStore.path = location.pathname;
	// 未登录跳转至登录页
	if (!await userStore.checkLogin()) {
		await router.push('/login');
	}
});
</script>
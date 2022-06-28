<!-- 根组件，仅挂载页面视图 -->
<template>
	<router-view class="root-page"/>
</template>

<script>
import { createNamespacedHelpers } from 'vuex';

const { mapActions: userActions } = createNamespacedHelpers('user');
const { mapActions: orgActions } = createNamespacedHelpers('organization');

export default {
	methods: {
		...userActions(['checkLogin']),
		...orgActions(['requestName'])
	},
	async created() {
		// 初始化后端配置数据
		await this.requestName();
		// 未登录跳转至登录页
		if (!await this.checkLogin()) {
			await this.$router.push('/login');
		}
	}
};
</script>
<template>

</template>

<script>
import { createNamespacedHelpers } from 'vuex';

const { mapState: userState, mapActions: userActions } = createNamespacedHelpers('user');

export default {
	data() {
		return {};
	},
	computed: {
		...userState(['userData'])
	},
	methods: {
		...userActions(['checkLogin'])
	},
	async mounted() {
		// 若未登录则跳转到登录页
		if (!await this.checkLogin()) {
			location.href = '/login';
			return;
		}
	}
};
</script>

<style lang="scss" scoped>

</style>
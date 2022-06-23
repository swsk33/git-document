<!--suppress CssUnknownTarget -->
<template>
	<div class="login-page">
		<login-panel></login-panel>
	</div>
</template>

<script>
import { sendRequest, REQUEST_METHOD } from '../../utils/request';
import LoginPanel from './components/LoginPanel.vue';

export default {
	components: {
		'login-panel': LoginPanel
	},
	data() {
		return {

		}
	},
	async mounted() {

	}
};
</script>

<style lang="scss" scoped>
.login-page {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100vh;
	background: url("/api/config-get/login-background") no-repeat center/cover;
}
</style>
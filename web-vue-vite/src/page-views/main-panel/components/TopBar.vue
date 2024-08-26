<template>
	<div class="top-bar" v-if="userStore.userData !== undefined">
		<el-avatar class="avatar" :src="parseAvatarURL(userStore.userData.avatar)" @click.stop="menuControl(true)"></el-avatar>
		<div class="menu" v-if="menuShow">
			<div class="info">{{ userStore.userData.nickname }} <br> <span class="role-name">{{ userStore.userData.role.showName }}</span></div>
			<ul class="menu-body">
				<li @click="toUserInfo">个人设置</li>
				<li @click="logout">退出登录</li>
			</ul>
		</div>
	</div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

let menuShow = ref(false);

// pinia
import { useUserStore } from '../../../store/user.js';
import { showNotification } from '../../../utils/message.js';
import { userLogout } from '../../../api/user-api.js';
import { parseAvatarURL } from '../../../api/image-api.js';

const userStore = useUserStore();

/**
 * 控制菜单显示隐藏
 * @param show 是否显示
 */
function menuControl(show) {
	menuShow.value = show;
}

/**
 * 进入个人中心页面
 */
function toUserInfo() {
	router.push('/my');
	menuShow.value = false;
}

/**
 * 用户退出
 */
async function logout() {
	await userLogout();
	await userStore.checkLogin();
	showNotification('成功', '已退出登录！');
	await router.push('/login');
}

// 定义组件引用暴露
defineExpose({
	menuControl
});
</script>

<style lang="scss" scoped>
.top-bar {
	position: relative;
	width: 100%;
	height: 6vh;
	background-color: rgba(75, 75, 163, 0.90);
	display: flex;
	align-items: center;

	.avatar {
		position: absolute;
		right: 1vw;
		user-select: none;
		cursor: pointer;
		width: 5vh;
		height: 5vh;
		border: 1px #ffd888 solid;
	}

	@keyframes menuLayout {
		from {
			top: -3vh;
			transform: scaleY(0);
		}

		to {
			top: calc(6vh + 2px);
			transform: scaleY(1);
		}
	}

	.menu {
		position: absolute;
		width: 20vw;
		top: calc(6vh + 2px);
		right: 1vw;
		border: 1px #dcdcdc solid;
		border-radius: 5px;
		background-color: rgba(255, 255, 255, 0.65);
		animation: menuLayout;
		animation-duration: 0.3s;
		animation-timing-function: ease-out;
		z-index: 10;

		.info {
			height: 72px;
			padding-left: 5%;
			padding-right: 5%;
			padding-top: 2%;
			box-sizing: border-box;
			border-bottom: 1px #dcdcdc solid;
			text-overflow: ellipsis;
			white-space: nowrap;
			overflow: hidden;
			font-size: 20px;
			color: #007c30;

			.role-name {
				color: #7462ff;
				font-weight: bold;
			}
		}

		.menu-body {
			margin: 0;
			padding: 0;

			li {
				height: 42px;
				font-size: 16px;
				line-height: 42px;
				list-style: none;
				padding-left: 5%;
				cursor: pointer;
				user-select: none;

				&:last-child {
					border-bottom-left-radius: 5px;
					border-bottom-right-radius: 5px;
				}

				&:hover {
					color: white;
					background-color: #2f3aff;
				}
			}
		}
	}
}
</style>
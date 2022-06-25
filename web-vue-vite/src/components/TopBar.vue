<template>
	<div class="top-bar">
		<el-avatar class="avatar" :src="avatar" @click.stop="menuControl(true)"></el-avatar>
		<div class="menu" v-if="menuShow">
			<div class="info">{{ nickname }} <br> <span class="role-name">{{ role }}</span></div>
			<ul class="menu-body">
				<li>个人设置</li>
				<li @click="userLogout">退出登录</li>
			</ul>
		</div>
	</div>
</template>

<script>
import { createNamespacedHelpers } from 'vuex';
import { sendRequest, REQUEST_METHOD } from '../utils/request.js';
import { ElNotification } from 'element-plus';

const { mapActions: userActions } = createNamespacedHelpers('user');

export default {
	data() {
		return {
			nickname: undefined,
			avatar: undefined,
			role: undefined,
			menuShow: false
		};
	},
	methods: {
		...userActions(['checkLogin']),
		/**
		 * 控制菜单显示隐藏
		 * @param show 是否显示
		 */
		menuControl(show) {
			this.menuShow = show;
		},
		/**
		 * 用户退出
		 */
		async userLogout() {
			const response = await sendRequest('/api/user/logout', REQUEST_METHOD.GET);
			await this.checkLogin();
			ElNotification({
						title: '成功',
						message: '已退出登录！',
						type: 'success',
						duration: 750
					}
			);
			setTimeout(() => {
				location.href = '/login';
			}, 1000);
		}
	}
};
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
		width: 4.5vh;
		height: 4.5vh;
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
		background-color: rgba(255, 255, 255, 0.5);
		animation: menuLayout;
		animation-duration: 0.3s;
		animation-timing-function: ease-out;
		z-index: 10;

		.info {
			height: 72px;
			padding-left: 5%;
			padding-right: 5%;
			padding-top: 5%;
			box-sizing: border-box;
			border-bottom: 1px #dcdcdc solid;
			text-overflow: ellipsis;
			white-space: nowrap;
			overflow: hidden;

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
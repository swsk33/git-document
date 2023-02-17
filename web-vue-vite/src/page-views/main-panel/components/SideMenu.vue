<template>
	<div class="side-menu" @click="$emit('closeAvatarMenu')">
		<div class="head">
			<Guide class="icon"/>
			<div class="title">Navigator</div>
		</div>
		<ul class="content">
			<li v-if="userStore.hasPermission('browse_article')" @click="router.push('/interior-anthology')">
				<Box class="icon"/>
				<div class="text">内部文章集</div>
			</li>
			<li v-if="userStore.hasPermission('edit_user')" @click="router.push('/user-manage')">
				<User class="icon"/>
				<div class="text">用户管理</div>
			</li>
			<li v-if="userStore.hasPermission('alter_system_setting')" @click="router.push('/system-setting')">
				<Operation class="icon"/>
				<div class="text">系统设置</div>
			</li>
		</ul>
	</div>
</template>

<script setup>
import { Guide, Box, User, Operation } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// pinia
import { useUserStore } from '../../../store/user';

const userStore = useUserStore();
</script>

<style lang="scss" scoped>
.side-menu {
	position: absolute;
	width: 16vw;
	height: 94vh;
	border-right: #d0d0d0 1px solid;
	background-color: rgba(255, 255, 255, 0.65);

	.head {
		display: flex;
		align-items: center;
		height: 6vh;
		font-weight: bold;
		background-color: rgba(196, 192, 255, 0.65);

		.icon {
			margin-left: 1vw;
			width: 4.5vh;
		}

		.title {
			margin-left: 1vw;
			font-size: 18px;
		}
	}

	.content {
		list-style: none;
		margin: 0;
		padding: 0;

		li {
			display: flex;
			align-items: center;
			height: 5.5vh;
			padding-left: 1.2vw;
			border-bottom: 1px #dcdcdc solid;
			box-sizing: border-box;
			user-select: none;
			cursor: pointer;
			transition-property: background-color, color;
			transition-duration: 1s;
			transition-timing-function: linear;

			.icon {
				height: 3.5vh;
			}

			.text {
				margin-left: 1.3vw;
			}

			&:active {
				color: white;
				background-color: #515bff;
				transition-duration: 0s;
			}
		}
	}
}
</style>
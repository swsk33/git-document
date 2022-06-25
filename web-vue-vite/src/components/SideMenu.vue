<template>
	<div class="side-menu">
		<div class="head">
			<Guide class="icon"/>
			<div class="title">Navigator</div>
		</div>
		<ul class="content">
			<li>
				<Box class="icon"/>
				<div class="text">内部文章集</div>
			</li>
			<li>
				<User class="icon"/>
				<div class="text">用户管理</div>
			</li>
		</ul>
	</div>
</template>

<script>
import { Guide, Box, User } from '@element-plus/icons-vue';

export default {
	components: {
		Guide,
		Box,
		User
	}
};
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

			.icon {
				height: 3.5vh;
			}

			.text {
				margin-left: 1.3vw;
			}
		}
	}
}
</style>
<template>
	<div class="anthology-panel">
		<div class="title">文集列表</div>
		<ul class="anthology-list">
			<li v-for="item in list" :key="item.id">
				<div class="image">
					<img :src="item.cover" alt="undefined"/>
				</div>
				<div class="text">{{ item.showName }}</div>
				<div class="button-box">
					<el-button type="primary" plain class="copy-ssh">复制Git SSH地址</el-button>
					<el-button type="warning" plain class="edit">编辑</el-button>
					<el-button type="success" plain class="go-to-read">去阅读</el-button>
				</div>
			</li>
		</ul>
	</div>
</template>

<script>
import { sendRequest, REQUEST_METHOD } from '../utils/request.js';
import { ElNotification } from 'element-plus';

export default {
	data() {
		return {
			list: []
		};
	},
	async mounted() {
		// 获取文集列表
		const response = await sendRequest('/api/anthology/get-all', REQUEST_METHOD.GET);
		if (!response.success) {
			ElNotification({
				title: '错误',
				message: '无法获取文集列表！请联系后端开发者！',
				type: 'error',
				duration: 1000
			});
			return;
		}
		this.list = response.data;
	}
};
</script>

<style lang="scss" scoped>
.anthology-panel {
	.title {
		height: 36px;
		line-height: 36px;
		padding-left: 2%;
		font-size: 18px;
		border-bottom: 1px solid #a1a1a1;
	}

	.anthology-list {
		margin: 0;
		padding: 0;

		li {
			position: relative;
			list-style: none;
			display: flex;
			justify-content: flex-start;
			align-items: center;
			height: 15vh;
			width: 96%;
			left: 2%;
			border-bottom: #d0d0d0 1px solid;
			box-sizing: border-box;

			.image {
				height: 10vh;
				width: 10vh;
				display: flex;
				justify-content: center;
				align-items: center;
				margin-left: 0.5%;

				img {
					max-width: 10vh;
					max-height: 10vh;
				}
			}

			.text {
				margin-left: 3%;
				width: 51%;
				height: 6vh;
				line-height: 6vh;
				font-size: 18px;
				white-space: nowrap;
				overflow: hidden;
				text-overflow: ellipsis;
			}

			.button-box {
				display: flex;
				justify-content: space-evenly;
				align-items: center;
				width: 33%;
				height: 6vh;
				margin-left: 3%;
			}

		}
	}
}
</style>
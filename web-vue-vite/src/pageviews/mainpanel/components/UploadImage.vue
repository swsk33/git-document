<template>
	<div class="upload-image">
		<div class="text">
			<slot name="text"></slot>
		</div>
		<div class="image-edit">
			<img class="preview" :src="previewImage" alt="无法显示"/>
			<!-- 通过拿到input组件的dom对象可以直接执行其点击方法即为click方法，这样就可以用自己的按钮触发上传事件 -->
			<el-button class="upload" type="success" plain @click="$refs.uploadCover.click()">上传</el-button>
			<input ref="uploadCover" type="file" @change="getImageFile($event)"/>
			<el-button class="random" type="warning" plain @click="getRandom">随机</el-button>
		</div>
	</div>
</template>

<script>
import { REQUEST_METHOD, sendRequest } from '../../../utils/request.js';
import { ElNotification } from 'element-plus';

export default {
	data() {
		return {
			// 预上传图片
			beforeUploadCover: undefined,
			// 预览图
			previewImage: undefined
		};
	},
	props: ['uploadUrl', 'randomUrl', 'initImage', 'uploadName'],
	methods: {
		/**
		 * 获取选择的文件并显示到预览图
		 */
		getImageFile(e) {
			// 设定备选上传的文件
			this.beforeUploadCover = e.target.files[0];
			// 读取文件以预览
			const reader = new FileReader();
			reader.onload = () => {
				this.previewImage = reader.result;
			};
			reader.readAsDataURL(this.beforeUploadCover);
		},
		/**
		 * 获取随机封面
		 */
		async getRandom() {
			const response = await sendRequest(this.randomUrl, REQUEST_METHOD.GET);
			if (!response.success) {
				ElNotification({
					title: '错误',
					message: '无法获取随机封面！请联系后端开发者！',
					type: 'error',
					duration: 1000
				});
				return;
			}
			this.previewImage = response.data;
			ElNotification({
				title: '成功',
				message: '获取随机封面成功！',
				type: 'success',
				duration: 1000
			});
		},
		/**
		 * 上传图片
		 */
		async upload() {
			// 创建表单对象
			let form = new FormData();
			form.append(this.uploadName, this.beforeUploadCover);
			const response = await sendRequest(this.uploadUrl, REQUEST_METHOD.POST, form);
			if (!response.success) {
				ElNotification({
					title: '失败',
					message: response.message,
					type: 'error',
					duration: 1000
				});
				return;
			}
			ElNotification({
				title: '成功',
				message: '上传图片成功！',
				type: 'success',
				duration: 1000
			});
			this.previewImage = response.data;
		}
	},
	mounted() {
		this.previewImage = this.initImage;
	}
};
</script>

<style lang="scss" scoped>
.upload-image {
	position: relative;
	width: 90%;
	display: flex;
	justify-content: space-evenly;
	align-items: center;

	.text {
		width: 25%;
		text-align: right;
	}

	.image-edit {
		display: flex;
		align-items: center;
		justify-content: space-evenly;
		width: 65%;

		input {
			display: none;
		}

		.preview {
			position: relative;
			max-width: 75px;
			max-height: 75px;
			border-radius: 17px;
			border: #e844ff 1px solid;
		}
	}
}
</style>
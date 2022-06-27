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
			beforeUploadImage: undefined,
			// 预览图
			previewImage: undefined
		};
	},
	// 分别是：上传API、随机获取API、初始图像、上传表单项名，initImage传入'init-random'时将使用随机获取的图片
	props: ['uploadUrl', 'randomUrl', 'initImage', 'uploadName'],
	methods: {
		/**
		 * 获取选择的文件并显示到预览图
		 */
		getImageFile(e) {
			// 设定备选上传的文件
			this.beforeUploadImage = e.target.files[0];
			// 读取文件以预览
			const reader = new FileReader();
			reader.onload = () => {
				this.previewImage = reader.result;
			};
			reader.readAsDataURL(this.beforeUploadImage);
		},
		/**
		 * 获取随机图片
		 * @param showTip 是否显示提示
		 */
		async getRandom(showTip = true) {
			this.beforeUploadImage = undefined;
			const response = await sendRequest(this.randomUrl, REQUEST_METHOD.GET);
			if (!response.success) {
				ElNotification({
					title: '错误',
					message: '无法获取随机图片！请联系后端开发者！',
					type: 'error',
					duration: 1000
				});
				return;
			}
			this.previewImage = response.data;
			if (showTip) {
				ElNotification({
					title: '成功',
					message: '获取随机图片成功！',
					type: 'success',
					duration: 1000
				});
			}
			return this.previewImage;
		},
		/**
		 * 上传图片并获取图片URL
		 * @return 若没有选择上传的文件，则返回传入的原有图像地址，否则上传图片并返回地址
		 */
		async uploadAndGetUrl() {
			if (this.beforeUploadImage === undefined) {
				return this.previewImage;
			}
			// 创建表单对象
			let form = new FormData();
			form.append(this.uploadName, this.beforeUploadImage);
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
			return response.data;
		}
	},
	async mounted() {
		this.previewImage = this.initImage;
		if (this.initImage === 'init-random') {
			this.previewImage = await this.getRandom(false);
		}
	}
};
</script>

<style lang="scss">
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
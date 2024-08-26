<template>
	<div class="upload-image">
		<div class="text">
			<slot name="text"></slot>
		</div>
		<div class="image-edit">
			<img class="preview" :src="previewImage" alt="无法显示"/>
			<!-- 通过拿到input组件的dom对象可以直接执行其点击方法即为click方法，这样就可以用自己的按钮触发上传事件 -->
			<el-button class="upload" type="success" plain @click="uploadButton.click()">上传</el-button>
			<input ref="uploadButton" type="file" @change="getImageFile($event)"/>
		</div>
	</div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { MESSAGE_TYPE, showNotification } from '../../../utils/message.js';
import { imageUpload, parseImageURL } from '../../../api/image-api.js';

// 引用上传按钮
const uploadButton = ref(null);

// 预上传图片
const beforeUploadImage = ref(undefined);
// 预览图
const previewImage = ref(undefined);

/**
 * 传入参数，如下：<br>
 * <ul>
 *   <li>initImage：初始化图像文件名，用于从后端请求图片</li>
 *   <li>defaultImage：默认图片资源，如果initImage为空，则显示该默认图片，为import的图片资源路径</li>
 *   <li>uploadName：上传文件时，表单文件项使用的字段名</li>
 * </ul>
 */
const props = defineProps(['initImage', 'defaultImage', 'uploadName']);

/**
 * 获取选择的文件并显示到预览图
 */
function getImageFile(e) {
	// 设定备选上传的文件
	beforeUploadImage.value = e.target.files[0];
	// 读取文件以预览
	const reader = new FileReader();
	reader.onload = () => {
		previewImage.value = reader.result;
	};
	reader.readAsDataURL(beforeUploadImage.value);
}

/**
 * 上传图片并获取图片文件名
 * @return {String} 若没有选择上传的文件，则返回传入的原有图像文件名，否则上传图片并返回地址
 */
async function uploadAndGetUrl() {
	// 如果位指定上传文件，则返回null表示不做更改
	if (beforeUploadImage.value == null) {
		return null;
	}
	// 创建表单对象
	const response = await imageUpload(props.uploadName, beforeUploadImage.value);
	if (!response.success) {
		showNotification('失败', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
	return response.data;
}

/**
 * 刷新预览图
 * @param {String} imageName 传入图片名刷新预览图，若不传入则使用props中的值
 */
function refreshPreviewImage(imageName = undefined) {
	if (imageName === undefined) {
		previewImage.value = props.initImage == null ? props.defaultImage : parseImageURL(props.initImage);
		return;
	}
	previewImage.value = parseImageURL(imageName);
}

// 定义组件暴露
defineExpose({
	uploadAndGetUrl, refreshPreviewImage
});

onMounted(() => {
	refreshPreviewImage();
});
</script>

<style lang="scss">
.upload-image {
	position: relative;
	width: 95%;
	display: flex;
	justify-content: space-between;
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
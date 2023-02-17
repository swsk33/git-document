<template>
	<div class="system-setting">
		<div class="title">系统设置</div>
		<!-- 背景图片 -->
		<div class="background-image">
			<div class="text">背景图片设置</div>
			<div class="tip">若上传自定义图片后未生效，请清除浏览器缓存！</div>
			<div class="box">
				<div class="login-image">
					<div class="text">登录页背景图</div>
					<el-button class="upload" type="success" plain @click="uploadLoginImage.click()">自定义</el-button>
					<el-button class="reset" type="warning" plain @click="resetImage('/api/system-setting/reset-login-image')">恢复默认</el-button>
					<input ref="uploadLoginImage" type="file" @change="getImage($event, 'login')"/>
				</div>
				<div class="main-image">
					<div class="text">主页面背景图</div>
					<el-button class="upload" type="primary" plain @click="uploadMainImage.click()">自定义</el-button>
					<el-button class="reset" type="danger" plain @click="resetImage('/api/system-setting/reset-main-image')">恢复默认</el-button>
					<input ref="uploadMainImage" type="file" @change="getImage($event, 'main')"/>
				</div>
			</div>
		</div>
		<!-- 文集恢复 -->
		<div class="anthology-recovery">
			<div class="text">恢复文集</div>
			<div class="tip">若服务器上存在文集Git仓库但是未显示在文集列表中，请点击下列恢复按钮</div>
			<el-button class="recovery" type="primary" size="large" @click="showRecoveryDialog">恢复</el-button>
			<!-- 恢复窗口 -->
			<InfoDialog class="recovery-dialog" ref="recoveryDialog" v-loading="loadingRecovery" :element-loading-text="loadingText">
				<template v-slot:title>文集恢复</template>
				<template v-slot:content>
					<div class="text">下列文集可以恢复：</div>
					<ul class="recovery-list">
						<li v-for="item in recoveryAnthology">{{ item.name }}</li>
					</ul>
				</template>
				<template v-slot:button-box>
					<el-button class="ok" type="success" @click="doAnthologyRecovery">确定</el-button>
					<el-button class="cancel" type="warning" @click="recoveryDialog.frameShow = false">取消</el-button>
				</template>
			</InfoDialog>
		</div>
	</div>
</template>

<script setup>
import { reactive, ref, watch } from 'vue';
import { REQUEST_METHOD, sendRequest } from '../../../utils/request';
import { ElNotification } from 'element-plus';
import axios from 'axios';

// 组件引入
import InfoDialog from '../components/InfoDialog.vue';

const recoveryDialog = ref(null);
const uploadLoginImage = ref(null);
const uploadMainImage = ref(null);

// 自定义响应式变量
// 待上传的图片
const uploadPicture = reactive({
	login: undefined,
	main: undefined
});

// 待恢复的文集列表
const recoveryAnthology = ref([]);
const loadingRecovery = ref(false);
const loadingText = ref(undefined);

// 自定义方法

/**
 * 读取图片
 */
function getImage(e, name) {
	// 设定备选上传的文件
	uploadPicture[name] = e.target.files[0];
	if (uploadPicture[name] === undefined) {
		return;
	}
	const reader = new FileReader();
	// 读取文件以预览
	reader.readAsDataURL(uploadPicture[name]);
}

/**
 * 上传图片
 * @param url 上传图片的地址
 * @param name 上传图片参数名
 * @param image 图片对象
 */
async function uploadImage(url, name, image) {
	let form = new FormData();
	form.append(name, image);
	const requestParam = {
		url: url,
		method: 'POST',
		headers: {
			'content-type': 'multipart/form-data'
		},
		data: form
	};
	const response = await axios(requestParam);
	if (!response.data.success) {
		ElNotification({
			title: '失败',
			message: response.data.message,
			type: 'error',
			duration: 1000
		});
		return;
	}
	ElNotification({
		title: '成功',
		message: response.data.message,
		type: 'success',
		duration: 1000
	});
}

/**
 * 恢复默认背景
 * @param {string} url 重置背景图的接口地址
 */
async function resetImage(url) {
	const response = await sendRequest(url, REQUEST_METHOD.GET);
	if (!response.success) {
		ElNotification({
			title: '错误',
			message: '重置背景图失败！请联系后端开发者！',
			type: 'error',
			duration: 1000
		});
		return;
	}
	ElNotification({
		title: '成功',
		message: response.message,
		type: 'success',
		duration: 1000
	});
}

/**
 * 打开文集恢复对话框
 */
async function showRecoveryDialog() {
	loadingText.value = '正在获取可恢复的文集列表...';
	loadingRecovery.value = true;
	recoveryDialog.value.frameShow = true;
	const response = await sendRequest('/api/anthology/get-not-in-database', REQUEST_METHOD.GET);
	loadingRecovery.value = false;
	if (!response.success) {
		ElNotification({
			title: '错误',
			message: '获取失败！请联系后端开发者！',
			type: 'error',
			duration: 3000
		});
		recoveryDialog.value.frameShow = false;
		return;
	}
	recoveryAnthology.value = response.data;
	if (recoveryAnthology.value.length === 0) {
		ElNotification({
			title: '完成',
			message: '没有可供恢复的文集！',
			type: 'warning',
			duration: 3000
		});
		recoveryDialog.value.frameShow = false;
		return;
	}
	ElNotification({
		title: '成功',
		message: response.message,
		type: 'success',
		duration: 3000
	});
}

/**
 * 执行恢复操作
 */
async function doAnthologyRecovery() {
	loadingText.value = '正在执行恢复...';
	loadingRecovery.value = true;
	const response = await sendRequest('/api/anthology/batch-add', REQUEST_METHOD.POST, recoveryAnthology.value);
	loadingRecovery.value = false;
	recoveryDialog.value.frameShow = false;
	if (!response.success) {
		ElNotification({
			title: '错误',
			message: response.message,
			type: 'error',
			duration: 3000
		});
		return;
	}
	ElNotification({
		title: '成功',
		message: '恢复成功！可自行去主页面修改文集信息！',
		type: 'success',
		duration: 2000
	});
}

// 监听器
// 监听是否选择了上传的登录背景图片
watch(() => uploadPicture.login, (newValue, oldValue) => {
	if (newValue === undefined) {
		return;
	}
	uploadImage('/api/system-setting/set-login-image', 'image', uploadPicture.login);
});

// 监听是否选择了上传的主面板背景图片
watch(() => uploadPicture.main, (newValue, oldValue) => {
	if (newValue === undefined) {
		return;
	}
	uploadImage('/api/system-setting/set-main-image', 'image', uploadPicture.main);
});
</script>

<style lang="scss" scoped>
.system-setting {
	.title {
		position: relative;
		height: 10%;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 32px;
	}

	.background-image, .anthology-recovery {
		position: relative;
		display: flex;
		flex-direction: column;
		align-items: center;

		> .text {
			font-size: 24px;
		}

		.tip {
			font-size: 13px;
			color: red;
			margin-top: 2px;
		}
	}

	.background-image {
		.box {
			position: relative;
			display: flex;
			justify-content: space-evenly;
			align-items: center;
			width: 80%;
			margin-top: 2%;

			.login-image, .main-image {
				position: relative;
				display: flex;
				align-items: center;

				.text {
					margin-right: 16px;
				}

				input {
					display: none;
				}
			}
		}
	}

	.anthology-recovery {
		margin-top: 2%;

		.recovery {
			margin-top: 1.5%;
			font-size: 18px;
		}

		.recovery-dialog {
			:deep(.content) {
				justify-content: flex-start;
			}

			.text {
				font-size: 20px;
			}

			.recovery-list {
				li {
					font-size: 18px;
					color: #140068;
				}
			}
		}
	}
}
</style>
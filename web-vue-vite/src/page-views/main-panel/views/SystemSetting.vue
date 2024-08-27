<template>
	<div class="system-setting">
		<div class="title">系统设置</div>
		<!-- 基本系统设置 -->
		<div class="setting-component basic-setting">
			<div class="text">基本系统设置</div>
			<div class="box">
				<div class="set-name">
					<div class="text">组织名</div>
					<el-input class="input" placeholder="请输入组织名" v-model="basicSetting.organizationName"/>
					<el-button class="ok" type="primary" plain @click="setOrganizationName">设定</el-button>
				</div>
				<div class="set-allow-public">
					<div class="text">允许公开注册</div>
					<el-switch class="switch" v-model="basicSetting.allowPublic" inline-prompt :active-icon="Check" :inactive-icon="Close"/>
				</div>
			</div>
		</div>
		<!-- 背景图片 -->
		<div class="setting-component background-image">
			<div class="text">背景图片设置</div>
			<div class="tip">若上传自定义图片后未生效，请清除浏览器缓存！</div>
			<div class="box">
				<div class="login-image">
					<div class="text">登录页背景图</div>
					<el-button class="upload" type="success" plain @click="uploadLoginImage.click()">自定义</el-button>
					<el-button class="reset" type="warning" plain @click="resetImage('login')">恢复默认</el-button>
					<input ref="uploadLoginImage" type="file" @change="uploadAndSetImage($event, 'login')"/>
				</div>
				<div class="main-image">
					<div class="text">主页面背景图</div>
					<el-button class="upload" type="primary" plain @click="uploadMainImage.click()">自定义</el-button>
					<el-button class="reset" type="danger" plain @click="resetImage('main')">恢复默认</el-button>
					<input ref="uploadMainImage" type="file" @change="uploadAndSetImage($event, 'main')"/>
				</div>
			</div>
		</div>
		<!-- 文集恢复 -->
		<div class="setting-component anthology-recovery">
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
					<el-button class="ok" type="success" size="large" @click="doAnthologyRecovery">确定</el-button>
					<el-button class="cancel" type="warning" size="large" @click="recoveryDialog.frameShow = false">取消</el-button>
				</template>
			</InfoDialog>
		</div>
	</div>
</template>

<script setup>
import { reactive, ref, watch } from 'vue';
import { MESSAGE_TYPE, showNotification } from '../../../utils/message.js';
import { systemResetLoginImage, systemResetMainImage, systemSetAllowPublic, systemSetLoginImage, systemSetMainImage, systemSetOrganizationName } from '../../../api/system-setting-api.js';
import { anthologyGetNotInDB, anthologyRestore } from '../../../api/anthology-api.js';

// 组件引入
import InfoDialog from '../components/InfoDialog.vue';
import { Check, Close } from '@element-plus/icons-vue';

const recoveryDialog = ref(null);
const uploadLoginImage = ref(null);
const uploadMainImage = ref(null);

// pinia
import { useMetaDataStore } from '../../../store/meta-data.js';

const metaStore = useMetaDataStore();

// 待恢复的文集列表
const recoveryAnthology = ref([]);
const loadingRecovery = ref(false);
const loadingText = ref(undefined);

// 基本系统设置
const basicSetting = reactive({
	organizationName: metaStore.organizationName,
	allowPublic: metaStore.allowPublic
});

/**
 * 设定组织名
 */
async function setOrganizationName() {
	if (basicSetting.organizationName == null || basicSetting.organizationName === '') {
		showNotification('失败', '组织名不能为空！', MESSAGE_TYPE.error);
		return;
	}
	const response = await systemSetOrganizationName(basicSetting.organizationName);
	if (!response.success) {
		showNotification('失败', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
	// 重新拉取组织名
	await metaStore.requestName();
}

/**
 * 监听允许公开设置是否更改，若更改则发送请求
 */
watch(() => basicSetting.allowPublic, async () => {
	const response = await systemSetAllowPublic(basicSetting.allowPublic);
	if (!response.success) {
		showNotification('失败', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
	// 重新拉取是否公开
	await metaStore.requestAllowPublic();
});

/**
 * 上传图片并设定背景图
 * @param {Event} e 表示监听按钮事件参数，用于获取选择的文件
 * @param {String} name 表示登录还是主页背景，登录为login，主页为main
 */
async function uploadAndSetImage(e, name) {
	const uploadImage = e.target.files[0];
	if (uploadImage == null) {
		return;
	}
	// 上传并设定图片
	let response;
	switch (name) {
		case 'main':
			response = await systemSetMainImage(uploadImage);
			break;
		case 'login':
			response = await systemSetLoginImage(uploadImage);
			break;
	}
	if (!response.success) {
		showNotification('失败', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', '自定义背景图成功！1.5s后页面刷新...');
	// 刷新页面
	setTimeout(() => {
		location.reload();
	}, 1500);
}

/**
 * 重置背景图
 * @param {String} name 表示登录还是主页背景，登录为login，主页为main
 */
async function resetImage(name) {
	let response;
	switch (name) {
		case 'main':
			response = await systemResetMainImage();
			break;
		case 'login':
			response = await systemResetLoginImage();
			break;
	}
	if (!response.success) {
		showNotification('错误', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', '重置背景图成功！1.5s后页面刷新...');
	// 刷新页面
	setTimeout(() => {
		location.reload();
	}, 1500);
}

/**
 * 打开文集恢复对话框
 */
async function showRecoveryDialog() {
	loadingText.value = '正在获取可恢复的文集列表...';
	loadingRecovery.value = true;
	recoveryDialog.value.frameShow = true;
	const response = await anthologyGetNotInDB();
	loadingRecovery.value = false;
	if (!response.success) {
		showNotification('错误', response.message, MESSAGE_TYPE.error);
		recoveryDialog.value.frameShow = false;
		return;
	}
	recoveryAnthology.value = response.data;
	if (recoveryAnthology.value.length === 0) {
		showNotification('提示', '没有需要恢复的文集！', MESSAGE_TYPE.warning);
		recoveryDialog.value.frameShow = false;
		return;
	}
	showNotification('成功', response.message);
}

/**
 * 执行恢复操作
 */
async function doAnthologyRecovery() {
	loadingText.value = '正在执行恢复...';
	loadingRecovery.value = true;
	const response = await anthologyRestore(recoveryAnthology.value);
	loadingRecovery.value = false;
	recoveryDialog.value.frameShow = false;
	if (!response.success) {
		showNotification('错误', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', '恢复成功！可自行去主页面修改文集信息！');
}
</script>

<style lang="scss" scoped>
.system-setting {
	.title {
		position: relative;
		height: 12%;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 36px;
	}

	.setting-component {
		position: relative;
		display: flex;
		flex-direction: column;
		align-items: center;
		margin-bottom: 2%;

		> .text {
			font-size: 24px;
		}

		.tip {
			font-size: 13px;
			color: red;
			margin-top: 2px;
		}
	}

	// 基本设置组件
	.basic-setting {
		.text {
			margin-bottom: 1.5%;
		}

		.box {
			display: flex;
			justify-content: space-evenly;
			align-items: center;
			width: 90%;

			.set-name, .set-allow-public {
				display: flex;
				justify-content: space-evenly;
				align-items: center;
				width: 35%;
			}

			.set-name {
				.text {
					width: 20%;
					text-align: center;
				}

				.input {
					width: 35%;
				}
			}
		}
	}

	// 背景图片设置组件
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

	// 文集恢复组件
	.anthology-recovery {
		.recovery {
			margin-top: 1.5%;
			font-size: 18px;
		}

		// 文集恢复窗口
		.recovery-dialog {
			:deep(.content) {
				justify-content: flex-start;
			}

			.text {
				font-size: 20px;
			}

			.recovery-list {
				border: purple 1px solid;
				border-radius: 6px;
				width: 80%;
				height: 90%;
				list-style: none;
				padding: 0 3px;

				li {
					padding: 2px 10px;
					font-size: 20px;
					line-height: 32px;
					color: #140068;
					border-bottom: 1px dashed green;
				}
			}

			.button-box {
				.ok, .cancel {
					font-size: 18px;
				}
			}
		}
	}
}
</style>
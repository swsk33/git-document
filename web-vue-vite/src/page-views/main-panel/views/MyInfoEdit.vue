<template>
	<div class="my-info">
		<div class="title">个人中心</div>
		<!-- 用户信息 -->
		<div class="content">
			<UploadImage class="avatar" ref="imageUpload" :init-image="editUserData.avatar" :default-image="parseAvatarURL(null)" upload-name="image">
				<template v-slot:text>头像</template>
			</UploadImage>
			<div class="username">
				<div class="text">用户名</div>
				<el-input class="input" v-model="editUserData.username" placeholder="请输入用户名"/>
			</div>
			<div class="password">
				<div class="text">密码</div>
				<el-input class="input" v-model="editUserData.password" show-password placeholder="不修改则留空"/>
			</div>
			<div class="nickname">
				<div class="text">昵称</div>
				<el-input class="input" v-model="editUserData.nickname" placeholder="请输入昵称"/>
			</div>
			<div class="email">
				<div class="text">邮箱</div>
				<el-input class="input" v-model="editUserData.email" placeholder="请输入邮箱"/>
			</div>
			<!-- 偏好设置 -->
			<div class="setting">
				<div class="text">个人偏好设置</div>
				<div class="box">
					<div class="item update-email-setting">
						<div class="text">收藏的文集更新时邮件通知我</div>
						<el-switch class="switch" v-model="editSetting.receiveUpdateEmail" inline-prompt :active-icon="Check" :inactive-icon="Close"/>
					</div>
					<div class="item create-email-setting">
						<div class="text">新文集发布时邮件通知我</div>
						<el-switch class="switch" v-model="editSetting.receiveNewEmail" inline-prompt :active-icon="Check" :inactive-icon="Close"/>
					</div>
				</div>
			</div>
			<!-- 公钥管理 -->
			<div class="public-key" v-if="userStore.hasPermission('edit_anthology')">
				<div class="header">
					<div class="text">公钥</div>
					<el-popover width="750" placement="left" title="添加SSH公钥" v-model:visible="popOverShow.publicKeyAdd">
						<template #reference>
							<el-button class="add" type="success" size="small" @click="popOverShow.publicKeyAdd = true">添加SSH公钥</el-button>
						</template>
						<el-input v-model="addPublicKey" type="textarea" cols="50" rows="5" resize="none" placeholder="请粘贴公钥内容至此"/>
						<div class="button-box" style="display: flex;margin-top: 12px">
							<el-button type="success" size="small" @click="addPublicKeyRequest">确认</el-button>
							<el-button type="warning" size="small" @click="popOverShow.publicKeyAdd = false">取消</el-button>
						</div>
					</el-popover>
				</div>
				<el-table class="data" :data="userStore.userData.keys" border empty-text="暂无公钥，添加公钥后才能推送文章！">
					<el-table-column width="50" :resizable="false" label="id" prop="id" align="center"/>
					<el-table-column width="600" show-overflow-tooltip label="公钥内容" prop="content"/>
					<el-table-column class-name="column-operate" align="center" label="操作">
						<template #default="scope">
							<el-popconfirm title="确认删除？" confirm-button-text="是的" cancel-button-text="再想想" confirm-button-type="danger" cancel-button-type="success" @confirm="deleteKey(scope.row.id)">
								<template #reference>
									<el-button size="small" type="danger">删除</el-button>
								</template>
							</el-popconfirm>
						</template>
					</el-table-column>
				</el-table>
			</div>
		</div>
		<el-button class="save" plain type="primary" @click="updateUserData">保存修改</el-button>
	</div>
</template>

<script setup>
import { Check, Close } from '@element-plus/icons-vue';
import { onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { MESSAGE_TYPE, showNotification } from '../../../utils/message.js';
import { publicKeyAdd, publicKeyDelete } from '../../../api/public-key-api.js';
import { userUpdate } from '../../../api/user-api.js';
import { parseAvatarURL } from '../../../api/image-api.js';
import { settingUpdate } from '../../../api/setting-api.js';

const router = useRouter();

// 组件
import UploadImage from '../components/UploadImage.vue';

const imageUpload = ref(null);

// pinia
import { useUserStore } from '../../../store/user.js';

const userStore = useUserStore();

/**
 * 显示弹窗
 */
const popOverShow = reactive({
	publicKeyAdd: false
});

/**
 * 被编辑的用户、设置和公钥信息
 */
const editDataObject = reactive({
	/**
	 * 用户信息
	 */
	user: {
		id: undefined,
		username: undefined,
		password: undefined,
		nickname: undefined,
		avatar: undefined,
		email: undefined
	},
	/**
	 * 用户偏好设定
	 */
	setting: {
		userId: undefined,
		receiveUpdateEmail: undefined,
		receiveNewEmail: undefined
	},
	/**
	 * 待添加的公钥
	 */
	addPublicKey: undefined
});

/**
 * 从userStore中刷新编辑信息
 */
async function refreshEditDataObject() {
	// 为空则刷新
	if (userStore.userData == null) {
		await userStore.checkLogin();
	}
	// 填充用户信息
	editDataObject.user.id = userStore.userData.id;
	editDataObject.user.username = userStore.userData.username;
	editDataObject.user.nickname = userStore.userData.nickname;
	editDataObject.user.avatar = userStore.userData.avatar;
	editDataObject.user.email = userStore.userData.email;
	// 填充偏好设置
	editDataObject.setting.userId = userStore.userData.id;
	editDataObject.setting.receiveUpdateEmail = userStore.userData.setting.receiveUpdateEmail;
	editDataObject.setting.receiveNewEmail = userStore.userData.setting.receiveNewEmail;
	// 刷新头像显示
	imageUpload.value.refreshPreviewImage(editDataObject.user.avatar);
}

/**
 * 删除公钥
 * @param id 公钥id
 */
async function deleteKey(id) {
	const response = await publicKeyDelete(id);
	if (!response.success) {
		showNotification('错误', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', '删除成功！');
}

/**
 * 发送增加公钥请求
 */
async function addPublicKeyRequest() {
	const response = await publicKeyAdd({
		content: editDataObject.addPublicKey
	});
	if (!response.success) {
		showNotification('错误', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
	popOverShow.publicKeyAdd = false;
	editDataObject.addPublicKey = undefined;
}

/**
 * 发送修改用户信息请求
 */
async function updateUserData() {
	editUserData.avatar = await imageUpload.value.uploadAndGetUrl();
	// 修改用户信息
	const updateUserResponse = await userUpdate(editUserData);
	// 修改用户设置
	const updateSettingResponse = await settingUpdate(editSetting);
	if (!updateUserResponse.success) {
		showNotification('错误', updateUserResponse.message, MESSAGE_TYPE.error);
		return;
	}
	if (!updateSettingResponse.success) {
		showNotification('错误', updateSettingResponse.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', '修改用户信息和偏好设置成功！');
	await router.push('/');
	// 刷新用户信息
	await userStore.checkLogin();
}

onMounted(async () => {
	// 加载页面时立刻刷新显示信息
	await refreshEditDataObject();
});
</script>

<style lang="scss" scoped>
.my-info {
	display: flex;
	flex-direction: column;
	justify-content: flex-start;
	align-items: center;

	.title {
		position: relative;
		font-size: 36px;
		height: 48px;
		line-height: 48px;
		margin-top: 3%;
	}

	.content {
		display: flex;
		flex-direction: column;
		justify-content: flex-start;
		align-items: center;
		margin-top: 3%;
		width: 90%;

		.avatar, .username, .password, .nickname, .email, .public-key {
			position: relative;
			display: flex;
			align-items: center;
			justify-content: flex-start;
			height: 48px;
			width: 75%;

			> .text {
				width: 8%;
				text-align: right;
				margin-right: 3%;
			}

			.input {
				width: 90%;
			}
		}

		.avatar {
			height: 100px;
		}

		.setting {
			display: flex;
			flex-direction: column;
			align-items: center;
			width: 75%;
			margin-top: 2%;

			> .text {
				color: #9000c9;
				font-size: 22px;
			}

			.box {
				width: 100%;
				display: flex;
				flex-direction: column;
				align-items: center;
				margin-top: 1%;

				.item {
					display: flex;
					align-items: center;
					justify-content: space-evenly;
					width: 100%;
					margin-bottom: 5px;

					.text {
						position: relative;
						width: 50%;
						text-align: center;
					}
				}
			}
		}

		.public-key {
			display: block;
			height: auto;
			margin-top: 3%;

			.header {
				display: flex;
				justify-content: space-between;
				align-items: center;
				margin-bottom: 1vh;

				.text {
					position: relative;
					font-size: 18px;
					color: #4f43b7;
				}
			}

			.data {
				position: relative;
				border: #2f3aff 1px solid;
				border-radius: 5px;
			}
		}
	}

	.save {
		position: relative;
		margin-top: 2vh;
		margin-bottom: 2vh;
	}
}
</style>
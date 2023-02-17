<template>
	<div class="my-info">
		<div class="title">个人中心</div>
		<div class="content">
			<UploadImage class="avatar" upload-url="/api/image/upload-avatar" random-url="/api/image/random-avatar" upload-name="avatar" ref="imageUpload">
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
			<div class="public-key" v-if="userStore.hasPermission('edit_anthology')">
				<div class="header">
					<div class="text">公钥</div>
					<el-popover width="750" placement="left" title="添加SSH公钥" v-model:visible="popOverShow.publicKeyAdd">
						<template #reference>
							<el-button class="add" type="success" size="small" @click="popOverShow.publicKeyAdd = true">添加SSH公钥</el-button>
						</template>
						<el-input v-model="addPublicKey" type="textarea" cols="50" rows="5" resize="none" placeholder="请粘贴公钥内容至此"></el-input>
						<div class="button-box" style="display: flex;margin-top: 12px">
							<el-button type="success" size="small" @click="addPublicKeyRequest">确认</el-button>
							<el-button type="warning" size="small" @click="popOverShow.publicKeyAdd = false">取消</el-button>
						</div>
					</el-popover>
				</div>
				<el-table class="data" :data="publicKeys" border empty-text="暂无公钥，添加公钥后才能推送文章！">
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
import { sendRequest, REQUEST_METHOD } from '../../../utils/request';
import { ElNotification } from 'element-plus';
import { onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// 组件
import UploadImage from '../components/UploadImage';

const imageUpload = ref(null);

// pinia
import { useUserStore } from '../../../store/user';

const userStore = useUserStore();

// 自定义响应式变量
const editUserData = reactive({
	id: undefined,
	avatar: undefined,
	email: undefined,
	username: undefined,
	nickname: undefined,
	password: undefined
});
const publicKeys = ref([]);
const addPublicKey = ref(undefined);
const popOverShow = reactive({
	publicKeyAdd: false
});

// 自定义方法

/**
 * 删除公钥
 * @param id 公钥id
 */
async function deleteKey(id) {
	const response = await sendRequest('/api/public-key/delete/' + id, REQUEST_METHOD.DELETE);
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
		message: '删除成功！',
		type: 'success',
		duration: 1000
	});
	await getPublicKeyList();
}

/**
 * 发送增加公钥请求
 */
async function addPublicKeyRequest() {
	const response = await sendRequest('/api/public-key/add', REQUEST_METHOD.POST, {
		content: addPublicKey.value
	});
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
		message: '添加SSH公钥成功！',
		type: 'success',
		duration: 1000
	});
	popOverShow.publicKeyAdd = false;
	addPublicKey.value = '';
	await getPublicKeyList();
}

/**
 * 发送修改用户信息请求
 */
async function updateUserData() {
	editUserData.avatar = await imageUpload.value.uploadAndGetUrl();
	const response = await sendRequest('/api/user/update', REQUEST_METHOD.PUT, editUserData);
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
		message: '修改用户信息成功！',
		type: 'success',
		duration: 1000
	});
	await router.push('/');
	// 刷新用户信息
	await userStore.checkLogin();
}

/**
 * 获取公钥列表
 */
async function getPublicKeyList() {
	const response = await sendRequest('/api/public-key/get-by-user', REQUEST_METHOD.GET);
	if (!response.success) {
		ElNotification({
			title: '失败',
			message: response.message,
			type: 'error',
			duration: 1000
		});
		return;
	}
	publicKeys.value = response.data;
}

onMounted(async () => {
	if (userStore.userData === undefined) {
		await userStore.checkLogin();
	}
	// 填充现有用户信息
	editUserData.id = userStore.userData.id;
	editUserData.avatar = userStore.userData.avatar;
	editUserData.email = userStore.userData.email;
	editUserData.username = userStore.userData.username;
	editUserData.nickname = userStore.userData.nickname;
	// 用户头像
	imageUpload.value.previewImage = editUserData.avatar;
	// 管理员用户获取公钥
	if (userStore.hasPermission('edit_anthology')) {
		await getPublicKeyList();
	}
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

		.public-key {
			display: block;
			height: auto;

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
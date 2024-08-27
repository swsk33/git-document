<template>
	<div class="user-manage" v-loading="!loadingDone" element-loading-text="正在拉取用户列表...">
		<div class="head">
			<div class="title">用户管理</div>
			<el-button class="add-user-button" type="success" @click="addUserDialog.frameShow = true">添加用户</el-button>
		</div>
		<!-- 全部用户列表 -->
		<el-table class="user-list" :data="userList" border row-class-name="user-list-row" empty-text="没有用户">
			<el-table-column prop="id" label="id" width="60" align="center"/>
			<el-table-column label="头像" width="120" :resizable="false" align="center">
				<template #default="scope">
					<img :src="parseAvatarURL(scope.row.avatar)" alt="无法显示">
				</template>
			</el-table-column>
			<el-table-column prop="username" show-overflow-tooltip label="用户名" width="250"/>
			<el-table-column prop="nickname" show-overflow-tooltip label="昵称" width="250"/>
			<el-table-column label="角色" width="150">
				<template #default="scope">
					<el-dropdown @command="changeUserRole">
						<div class="current-value" style="user-select: none">
							{{ scope.row.role.showName }}
							<el-icon class="arrow-down">
								<arrow-down/>
							</el-icon>
						</div>
						<template #dropdown>
							<el-dropdown-menu>
								<el-dropdown-item v-for="item in userStore.roleList" :command="{userId: scope.row.id, roleId: item.id}" style="user-select: none">{{ item.showName }}</el-dropdown-item>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
				</template>
			</el-table-column>
			<el-table-column label="操作" align="center">
				<template #default="scope">
					<el-popconfirm title="确定删除？" @confirm="deleteUser(scope.row.id)" confirm-button-type="danger" cancel-button-type="primary" confirm-button-text="确定" cancel-button-text="取消">
						<template #reference>
							<el-button type="danger">删除</el-button>
						</template>
					</el-popconfirm>
				</template>
			</el-table-column>
		</el-table>
		<!-- 信息弹窗 - 增加用户 -->
		<InfoDialog class="add-user-dialog" ref="addUserDialog">
			<template v-slot:title>添加用户</template>
			<template v-slot:content>
				<!-- 头像上传组件 -->
				<UploadImage class="avatar" ref="addUserAvatar" upload-name="image" :init-image="addUserData.avatar" :default-image="parseAvatarURL(null)">
					<template v-slot:text>设定头像</template>
				</UploadImage>
				<div class="username">
					<div class="text">用户名：</div>
					<el-input class="input" v-model="addUserData.username" placeholder="用户的登录凭证"/>
				</div>
				<div class="password">
					<div class="text">密码：</div>
					<el-input class="input" v-model="addUserData.password" show-password placeholder="用户的密码"/>
				</div>
				<div class="nickname">
					<div class="text">昵称：</div>
					<el-input class="input" v-model="addUserData.nickname" placeholder="用户的显示名称"/>
				</div>
				<div class="email">
					<div class="text">邮箱：</div>
					<el-input class="input" v-model="addUserData.email" placeholder="用户的邮箱"/>
				</div>
				<div class="role">
					<div class="text">角色：</div>
					<el-radio-group v-model="addUserData.role.id" class="input">
						<el-radio v-for="item in availableRole" :label="item.id" size="large">{{ item.showName }}</el-radio>
					</el-radio-group>
				</div>
			</template>
			<template v-slot:button-box>
				<el-button class="ok" type="success" size="large" @click="addUser">确定</el-button>
				<el-button class="cancel" type="warning" size="large" @click="addUserDialog.frameShow = false">取消</el-button>
			</template>
		</InfoDialog>
	</div>
</template>

<script setup>
import { ArrowDown } from '@element-plus/icons-vue';
import { onBeforeMount, reactive, ref } from 'vue';
import { MESSAGE_TYPE, showNotification } from '../../../utils/message.js';
import { userDelete, userGetAll, userRegister, userUpdate } from '../../../api/user-api.js';
import { parseAvatarURL } from '../../../api/image-api.js';

// 组件引入
import InfoDialog from '../components/InfoDialog.vue';
import UploadImage from '../components/UploadImage.vue';

const addUserDialog = ref(null);
const addUserAvatar = ref(null);

// pinia
import { useUserStore } from '../../../store/user.js';

const userStore = useUserStore();

/**
 * 是否加载完成
 */
const loadingDone = ref(false);

/**
 * 用户列表
 */
const userList = ref([]);

/**
 * 可用于添加新用户的角色
 */
const availableRole = ref([]);

/**
 * 添加的用户信息
 */
const addUserData = reactive({
	username: undefined,
	password: undefined,
	nickname: undefined,
	email: undefined,
	avatar: undefined,
	roleId: 2
});

/**
 * 获取全部用户列表
 */
async function getUserList() {
	loadingDone.value = false;
	const response = await userGetAll();
	loadingDone.value = true;
	if (!response.success) {
		showNotification('失败', response.message, MESSAGE_TYPE.error);
		return;
	}
	userList.value = response.data;
}

/**
 * 删除用户请求
 * @param id 用户id
 */
async function deleteUser(id) {
	if (userStore.userData.id === id) {
		showNotification('失败', '不能删除自己！', MESSAGE_TYPE.warning);
		return;
	}
	const response = await userDelete(id);
	if (!response.success) {
		showNotification('失败', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
	// 刷新列表
	await getUserList();
}

/**
 * 修改用户权限
 * @param ids 传入用户id和角色id，ids中有两个属性userId和roleId分别表示用户id与权限id
 */
async function changeUserRole(ids) {
	const response = await userUpdate({
		id: ids.userId,
		roleId: ids.roleId
	});
	if (!response.success) {
		showNotification('失败', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
	// 刷新列表
	await getUserList();
}

/**
 * 添加用户
 */
async function addUser() {
	addUserData.avatar = await addUserAvatar.value.uploadAndGetUrl();
	const response = await userRegister(addUserData);
	if (!response.success) {
		showNotification('失败', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
	// 刷新列表
	await getUserList();
	addUserDialog.value.frameShow = false;
}

onBeforeMount(async () => {
	await getUserList();
	await userStore.getRoleList();
	// 把全部角色列表先复制到可用角色列表中，并删除第一个预留管理员角色
	availableRole.value = [...userStore.roleList];
	availableRole.value.shift();
});
</script>

<style lang="scss" scoped>
.user-manage {
	.head {
		position: relative;
		height: 10%;
		display: flex;
		align-items: center;

		.title {
			font-size: 24px;
			margin-left: 2.5%;
		}

		.add-user-button {
			position: absolute;
			right: 3%;
		}
	}

	// 用户列表
	.user-list {
		position: relative;
		width: 96%;
		left: 2%;
		border: 1px #7c2fff solid;
		border-radius: 6px;

		.user-list-row {
			img {
				max-height: 75px;
				max-width: 75px;
				border-radius: 50%;
				border: #7462ff 2px solid;
				box-sizing: border-box;
			}
		}
	}

	// 添加用户弹窗
	.add-user-dialog {
		:deep(.content) {
			justify-content: flex-start;
		}

		.avatar, .username, .nickname, .password, .email, .role {
			position: relative;
			display: flex;
			justify-content: space-evenly;
			align-items: center;
			width: 90%;
			margin-bottom: 2.5%;

			.text {
				width: 20%;
				text-align: right;
			}

			.input {
				width: 70%;
				margin-left: 3%;
			}
		}

		.avatar {
			margin-bottom: 4.5%;
		}

		.button-box {
			.ok, .cancel {
				font-size: 18px;
			}
		}
	}
}
</style>
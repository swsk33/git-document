<template>
	<div class="user-manage">
		<div class="head">
			<div class="title">用户管理</div>
			<el-button class="add-user-button" type="success" @click="$refs.addUserDialog.frameShow = true">添加用户</el-button>
		</div>
		<el-table class="user-list" :data="userList" border row-class-name="user-list-row" empty-text="没有用户">
			<el-table-column prop="id" label="id" width="60" align="center"/>
			<el-table-column label="头像" width="120" :resizable="false" align="center">
				<template #default="scope">
					<img :src="scope.row.avatar" alt="无法显示">
				</template>
			</el-table-column>
			<el-table-column prop="username" show-overflow-tooltip label="用户名" width="250"/>
			<el-table-column prop="nickname" show-overflow-tooltip label="昵称" width="250"/>
			<el-table-column label="角色" width="120">
				<template #default="scope">
					<el-dropdown @command="changeUserRole">
						<div class="current-value">
							{{ scope.row.role.showName }}
						</div>
						<template #dropdown>
							<el-dropdown-menu>
								<el-dropdown-item v-for="item in roleList" :command="{userId:scope.row.id, roleId: item.id}">{{ item.showName }}</el-dropdown-item>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
				</template>
			</el-table-column>
			<el-table-column label="操作" align="center">
				<template #default="scope">
					<el-popconfirm title="确定重置？" @confirm="resetUser(scope.row.id)" confirm-button-type="danger" cancel-button-type="primary" confirm-button-text="确定" cancel-button-text="取消">
						<template #reference>
							<el-button type="warning">重置密码</el-button>
						</template>
					</el-popconfirm>
					<el-popconfirm title="确定删除？" @confirm="deleteUser(scope.row.id)" confirm-button-type="danger" cancel-button-type="primary" confirm-button-text="确定" cancel-button-text="取消">
						<template #reference>
							<el-button type="danger">删除</el-button>
						</template>
					</el-popconfirm>
				</template>
			</el-table-column>
		</el-table>
		<!-- 信息弹窗 - 增加用户 -->
		<info-dialog class="add-user-dialog" ref="addUserDialog">
			<template v-slot:title>添加用户</template>
			<template v-slot:content>
				<upload-image ref="addUserAvatar" upload-url="/api/image/upload-avatar" random-url="/api/image/random-avatar" upload-name="avatar" init-image="init-random">
					<template v-slot:text>设定头像</template>
				</upload-image>
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
						<el-radio v-for="item in roleList" :label="item.id" size="large">{{ item.showName }}</el-radio>
					</el-radio-group>
				</div>
			</template>
			<template v-slot:button-box>
				<el-button class="ok" type="success" @click="addUser">确定</el-button>
				<el-button class="cancel" type="warning" @click="$refs.addUserDialog.frameShow = false">取消</el-button>
			</template>
		</info-dialog>
	</div>
</template>

<script>
import { sendRequest, REQUEST_METHOD } from '../../../utils/request.js';
import { ElNotification } from 'element-plus';
import { createNamespacedHelpers } from 'vuex';

import infoDialog from '../components/InfoDialog.vue';
import uploadImage from '../components/UploadImage.vue';

const { mapState: userState, mapActions: userActions, mapGetters: userGetters } = createNamespacedHelpers('user');

export default {
	components: {
		'info-dialog': infoDialog,
		'upload-image': uploadImage
	},
	data() {
		return {
			userList: [],
			addUserData: {
				username: undefined,
				password: undefined,
				nickname: undefined,
				email: undefined,
				avatar: undefined,
				role: {
					id: 2
				}
			}
		};
	},
	computed: {
		...userState(['userData']),
		...userGetters(['roleList'])
	},
	methods: {
		...userActions(['getRoleList']),
		/**
		 * 获取全部用户列表
		 */
		async getUserList() {
			const response = await sendRequest('/api/user/get-all', REQUEST_METHOD.GET);
			if (!response.success) {
				ElNotification({
					title: '失败',
					message: response.message,
					type: 'error',
					duration: 1000
				});
				return;
			}
			this.userList = response.data;
		},
		/**
		 * 删除用户请求
		 * @param id 用户id
		 */
		async deleteUser(id) {
			if (this.userData.id === id) {
				ElNotification({
					title: '错误',
					message: '不能删除自己！',
					type: 'warning',
					duration: 1000
				});
				return;
			}
			const response = await sendRequest('/api/user/delete/' + id, REQUEST_METHOD.DELETE);
			if (!response.success) {
				ElNotification({
					title: '错误',
					message: response.message,
					type: 'error',
					duration: 1000
				});
				return;
			}
			ElNotification({
				title: '成功',
				message: '删除完成！',
				type: 'success',
				duration: 1000
			});
			// 刷新列表
			await this.getUserList();
		},
		/**
		 * 重置用户密码
		 * @param id 用户id
		 */
		async resetUser(id) {
			const response = await sendRequest('/api/user/admin-reset-password/' + id, REQUEST_METHOD.GET);
			if (!response.success) {
				ElNotification({
					title: '错误',
					message: response.message,
					type: 'error',
					duration: 1000
				});
				return;
			}
			ElNotification({
				title: '成功',
				message: '新密码已发送到用户邮箱！',
				type: 'success',
				duration: 1000
			});
		},
		/**
		 * 修改用户权限
		 * @param ids 传入用户id和角色id，ids中有两个属性userId和roleId分别表示用户id与权限id
		 */
		async changeUserRole(ids) {
			const response = await sendRequest('/api/user/update', REQUEST_METHOD.PUT, {
				id: ids.userId,
				role: {
					id: ids.roleId
				}
			});
			if (!response.success) {
				ElNotification({
					title: '错误',
					message: response.message,
					type: 'error',
					duration: 1000
				});
				return;
			}
			ElNotification({
				title: '成功',
				message: '修改用户权限成功！',
				type: 'success',
				duration: 1000
			});
			// 刷新列表
			await this.getUserList();
		},
		/**
		 * 添加用户
		 */
		async addUser() {
			this.addUserData.avatar = await this.$refs.addUserAvatar.uploadAndGetUrl();
			const response = await sendRequest('/api/user/register', REQUEST_METHOD.POST, this.addUserData);
			if (!response.success) {
				ElNotification({
					title: '错误',
					message: response.message,
					type: 'error',
					duration: 1000
				});
				return;
			}
			ElNotification({
				title: '成功',
				message: '添加用户成功！',
				type: 'success',
				duration: 1000
			});
			// 刷新列表
			await this.getUserList();
			this.$refs.addUserDialog.frameShow = false;
		}
	},
	async mounted() {
		await this.getUserList();
		await this.getRoleList();
	}
};
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

	.user-list {
		position: relative;
		width: 96%;
		left: 2%;
		border: 1px #7c2fff solid;
		border-radius: 6px;

		.user-list-row {
			img {
				max-height: 75px;
				border-radius: 50%;
				border: #7462ff 2px solid;
				box-sizing: border-box;
			}
		}
	}

	.add-user-dialog {
		.username, .nickname, .password, .email, .role {
			position: relative;
			display: flex;
			align-items: center;
			margin-top: 3%;

			.text {
				width: 128px;
				text-align: right;
			}

			.input {
				width: 135%;
				margin-left: 3%;
			}
		}

		.role {
			right: 2%;
		}
	}
}
</style>
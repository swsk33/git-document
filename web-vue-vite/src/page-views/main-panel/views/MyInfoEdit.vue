<template>
	<div class="my-info">
		<div class="title">个人中心</div>
		<div class="content">
			<upload-image class="avatar" upload-url="/api/image/upload-avatar" random-url="/api/image/random-avatar" upload-name="avatar" ref="imageUpload">
				<template v-slot:text>头像</template>
			</upload-image>
			<div class="username">
				<div class="text">用户名</div>
				<el-input class="input" v-model="editUserdata.username" placeholder="请输入用户名"/>
			</div>
			<div class="password">
				<div class="text">密码</div>
				<el-input class="input" v-model="editUserdata.password" show-password placeholder="不修改则留空"/>
			</div>
			<div class="nickname">
				<div class="text">昵称</div>
				<el-input class="input" v-model="editUserdata.nickname" placeholder="请输入昵称"/>
			</div>
			<div class="email">
				<div class="text">邮箱</div>
				<el-input class="input" v-model="editUserdata.email" placeholder="请输入邮箱"/>
			</div>
			<div class="public-key" v-if="hasPermission('edit_anthology')">
				<div class="text">公钥</div>
				<el-table class="data" :data="publicKeys" border empty-text="暂无公钥，请添加公钥后再推送文章！">
					<el-table-column width="50" :resizable="false" label="id" prop="id" align="center"/>
					<el-table-column width="800" show-overflow-tooltip label="公钥内容" prop="content"/>
					<el-table-column class-name="column-operate" align="center">
						<template #header>
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
						</template>
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

<script>
import { createNamespacedHelpers } from 'vuex';
import { sendRequest, REQUEST_METHOD } from '../../../utils/request.js';

import uploadImage from '../components/UploadImage.vue';
import { ElNotification } from 'element-plus';

const { mapState: userState, mapActions: userActions, mapGetters: userGetters } = createNamespacedHelpers('user');

export default {
	data() {
		return {
			editUserdata: {
				id: undefined,
				avatar: undefined,
				email: undefined,
				username: undefined,
				nickname: undefined,
				password: undefined
			},
			publicKeys: [],
			addPublicKey: undefined,
			popOverShow: {
				publicKeyAdd: false
			}
		};
	},
	components: {
		'upload-image': uploadImage
	},
	computed: {
		...userState(['userData']),
		...userGetters(['hasPermission'])
	},
	methods: {
		...userActions(['checkLogin']),
		/**
		 * 删除公钥
		 * @param id 公钥id
		 */
		async deleteKey(id) {
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
			await this.getPublicKeyList();
		},
		/**
		 * 发送增加公钥请求
		 */
		async addPublicKeyRequest() {
			const response = await sendRequest('/api/public-key/add', REQUEST_METHOD.POST, {
				content: this.addPublicKey
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
			this.popOverShow.publicKeyAdd = false;
			this.addPublicKey = '';
			await this.getPublicKeyList();
		},
		/**
		 * 发送修改用户信息请求
		 */
		async updateUserData() {
			this.editUserdata.avatar = await this.$refs.imageUpload.uploadAndGetUrl();
			const response = await sendRequest('/api/user/update', REQUEST_METHOD.PUT, this.editUserdata);
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
			await this.$router.push('/');
			// 刷新用户信息
			await this.checkLogin();
		},
		/**
		 * 获取公钥列表
		 */
		async getPublicKeyList() {
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
			this.publicKeys = response.data;
		}
	},
	async mounted() {
		if (this.userData === undefined) {
			await this.checkLogin();
		}
		this.editUserdata = {
			id: this.userData.id,
			username: this.userData.username,
			password: undefined,
			nickname: this.userData.nickname,
			avatar: this.userData.avatar,
			email: this.userData.email
		};
		this.$refs.imageUpload.previewImage = this.editUserdata.avatar;
		// 管理员用户获取公钥
		if (this.hasPermission('edit_anthology')) {
			await this.getPublicKeyList();
		}
	}
};
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

		.avatar, .username, .password, .nickname, .email {
			position: relative;
			display: flex;
			align-items: center;
			justify-content: flex-start;
			height: 48px;
			width: 75vw;

			.text {
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
			.text {
				position: relative;
				font-size: 18px;
				color: #4f43b7;
				width: 8%;
				left: 1%;
				margin-bottom: 1%;
			}

			.data {
				position: relative;
				border: #2f3aff 1px solid;
				border-radius: 6px;
				width: 75vw;
			}
		}
	}

	.save {
		margin-top: 2%;
	}
}
</style>
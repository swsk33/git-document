<template>
	<div class="anthology-panel" v-loading="!loadingDone" element-loading-text="正在拉取文集列表...">
		<div class="top-box">
			<div class="title">文集列表</div>
			<el-button type="primary" plain class="add" v-if="userStore.hasPermission('edit_anthology')" @click="addAnthologyRef.frameShow = true">增加文集</el-button>
		</div>
		<ul class="anthology-list">
			<li v-for="item in list" :key="item.id">
				<div class="image">
					<img :src="item.cover" alt="undefined"/>
				</div>
				<el-tooltip placement="top" :content="item.showName">
					<div class="text">{{ item.showName }}</div>
				</el-tooltip>
				<el-tooltip placement="top" content="最后更新时间">
					<div class="update-time">{{ getUpdateTime(item.updateTime) }}</div>
				</el-tooltip>
				<div class="button-box">
					<el-button type="primary" plain class="copy-ssh" :id="'copy-ssh-' + item.id" v-if="userStore.hasPermission('edit_anthology')" @click="copySSH(item.id, item.systemUser, item.repoPath, item.sshPort)">复制Git SSH地址</el-button>
					<el-button type="warning" plain class="edit" v-if="userStore.hasPermission('edit_anthology')" @click="showEditDialog(item)">编辑</el-button>
					<el-button type="success" plain class="go-to-read" @click="router.push('/article-menu/' + item.id)">去阅读</el-button>
				</div>
			</li>
		</ul>
		<div class="empty-text" v-if="list.length === 0">(=ﾟωﾟ)ﾉ没有文集！</div>
		<!-- 信息弹窗 - 增加文集 -->
		<InfoDialog class="add-anthology" ref="addAnthologyRef">
			<template v-slot:title>增加文集</template>
			<template v-slot:content>
				<div class="name">
					<div class="text">文集名：</div>
					<el-input class="input" v-model="addAnthologyInfo.name" placeholder="请输入文集名，由英文和数字组成"/>
				</div>
				<div class="show-name">
					<div class="text">文集显示名：</div>
					<el-input class="input" v-model="addAnthologyInfo.showName" placeholder="请输入文集显示名"/>
				</div>
			</template>
			<template v-slot:button-box>
				<el-button class="ok" type="success" @click="addAnthology">确定</el-button>
				<el-button class="cancel" type="warning" @click="addAnthologyRef.frameShow = false">取消</el-button>
			</template>
		</InfoDialog>
		<!-- 信息弹窗 - 修改文集 -->
		<InfoDialog class="edit-anthology" ref="editAnthologyRef">
			<template v-slot:title>修改文集</template>
			<template v-slot:content>
				<upload-image class="cover" upload-url="/api/image/upload-cover" random-url="/api/image/random-cover" :init-image="editAnthologyInfo.cover" upload-name="cover" ref="uploadImage">
					<template v-slot:text>上传封面：</template>
				</upload-image>
				<div class="show-name">
					<div class="text">文集显示名：</div>
					<el-input class="input" v-model="editAnthologyInfo.showName" placeholder="请输入文集显示名"/>
				</div>
			</template>
			<template v-slot:button-box>
				<el-button class="ok" type="success" @click="editAnthology">确定</el-button>
				<el-button class="cancel" type="warning" @click="editAnthologyRef.frameShow = false">取消</el-button>
				<el-popconfirm @confirm="deleteAnthology" title="确认删除这个文集？" cancel-button-text="取消" confirm-button-text="确认" cancel-button-type="primary" confirm-button-type="danger">
					<template #reference>
						<el-button type="danger" size="small" class="delete-button" plain>删除</el-button>
					</template>
				</el-popconfirm>
			</template>
		</InfoDialog>
	</div>
</template>

<script setup>
import ClipBoard from 'clipboard';

import { sendRequest, REQUEST_METHOD } from '../../../utils/request';
import { timestampToDateString } from '../../../utils/time-convert';
import { ElNotification } from 'element-plus';
import { reactive, ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// 组件
import InfoDialog from '../components/InfoDialog.vue';
import UploadImage from '../components/UploadImage.vue';

const addAnthologyRef = ref(null);
const editAnthologyRef = ref(null);
const uploadImage = ref(null);

// pinia
import { useUserStore } from '../../../store/user';

const userStore = useUserStore();

// 响应式变量
const list = ref([]);
const loadingDone = ref(false);
const addAnthologyInfo = reactive({
	name: undefined,
	showName: undefined
});
const editAnthologyInfo = reactive({
	id: undefined,
	showName: undefined,
	cover: undefined
});

// 计算属性
const getUpdateTime = computed(() => (timestamp) => {
	return timestampToDateString(timestamp);
});

/**
 * 获取文集列表
 */
async function getAnthologyList() {
	loadingDone.value = false;
	const response = await sendRequest('/api/anthology/get-all', REQUEST_METHOD.GET);
	loadingDone.value = true;
	if (!response.success) {
		ElNotification({
			title: '错误',
			message: '无法获取文集列表！请联系后端开发者！',
			type: 'error',
			duration: 1000
		});
		return;
	}
	list.value = response.data;
}

/**
 * 复制文集Git SSH克隆地址
 * @param id 传入文集id
 * @param systemUser 传入后台运行用户
 * @param repoPath 传入仓库路径
 * @param port 传入仓库暴露端口
 */
function copySSH(id, systemUser, repoPath, port) {
	const clipBoard = new ClipBoard('#copy-ssh-' + id, {
		text() {
			return port !== 22 ? 'ssh://' + systemUser + '@' + location.hostname + ':' + port + repoPath : systemUser + '@' + location.hostname + ':' + repoPath;
		}
	});
	clipBoard.on('success', () => {
		ElNotification({
			title: '成功',
			message: '复制成功！',
			type: 'success',
			duration: 1000
		});
		clipBoard.destroy();
	});
}

/**
 * 添加文集
 */
async function addAnthology() {
	const response = await sendRequest('/api/anthology/add', REQUEST_METHOD.POST, addAnthologyInfo);
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
		message: '添加文集成功！',
		type: 'success',
		duration: 1000
	});
	addAnthologyRef.value.frameShow = false;
	// 重新获取文集列表
	await getAnthologyList();
}

/**
 * 显示修改文集弹窗
 * @param anthology 文集对象
 */
function showEditDialog(anthology) {
	editAnthologyInfo.id = anthology.id;
	editAnthologyInfo.showName = anthology.showName;
	editAnthologyInfo.cover = anthology.cover;
	editAnthologyRef.value.frameShow = true;
}

/**
 * 修改文集信息
 */
async function editAnthology() {
	editAnthologyInfo.cover = await uploadImage.value.uploadAndGetUrl();
	// 修改文集数据
	const response = await sendRequest('/api/anthology/update', REQUEST_METHOD.PUT, editAnthologyInfo);
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
		message: '修改文集信息成功！',
		type: 'success',
		duration: 1000
	});
	editAnthologyRef.value.frameShow = false;
	// 刷新列表
	await getAnthologyList();
}

/**
 * 删除当前编辑的文集
 */
async function deleteAnthology() {
	const response = await sendRequest('/api/anthology/delete/' + editAnthologyInfo.id, REQUEST_METHOD.DELETE);
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
		message: '删除文集成功！',
		type: 'success',
		duration: 1000
	});
	editAnthologyRef.value.frameShow = false;
	// 刷新列表
	await getAnthologyList();
}

onMounted(async () => {
	// 挂载组件时获取文集列表
	await getAnthologyList();
});
</script>

<style lang="scss" scoped>
.anthology-panel {
	.top-box {
		height: 48px;
		line-height: 36px;
		font-size: 18px;
		border-bottom: 1px solid #a1a1a1;
		display: flex;
		justify-content: space-between;
		align-items: center;

		.title {
			position: relative;
			padding-left: 3%;
		}

		.add {
			position: relative;
			right: 4%;
		}
	}

	.anthology-list {
		margin: 0;
		padding: 0;

		li {
			position: relative;
			list-style: none;
			display: flex;
			justify-content: flex-start;
			align-items: center;
			height: 15vh;
			width: 96%;
			left: 2%;
			border-bottom: #d0d0d0 1px solid;
			box-sizing: border-box;

			.image {
				height: 10vh;
				width: 10vh;
				display: flex;
				justify-content: center;
				align-items: center;
				margin-left: 0.5%;
				box-sizing: border-box;

				img {
					max-width: 10vh;
					max-height: 10vh;
					border-radius: 17px;
					border: #e844ff 1px solid;
				}
			}

			.text, .update-time {
				white-space: nowrap;
				overflow: hidden;
				text-overflow: ellipsis;
				height: 6vh;
				line-height: 6vh;
			}

			.text {
				margin-left: 3%;
				width: 31%;
				font-size: 18px;
			}

			.update-time {
				width: 20%;
				font-size: 13px;
				text-align: center;
				color: #5500ff;
			}

			.button-box {
				display: flex;
				justify-content: space-evenly;
				align-items: center;
				width: 33%;
				height: 6vh;
				margin-left: 3%;
			}

		}
	}

	.empty-text {
		position: relative;
		font-size: 64px;
		color: #868686;
		text-align: center;
		top: 30%;
	}

	.add-anthology {
		:deep(.content) {
			left: 10%;
			width: 80%;
		}

		.content {
			.name, .show-name {
				position: relative;
				display: flex;
				align-items: center;
				width: 100%;

				.text {
					position: relative;
					width: 128px;
					text-align: right;
				}

				.input {
					position: relative;
					margin-left: 2%;
				}
			}
		}
	}

	.edit-anthology {
		:deep(.content) {
			width: 70%;
			left: 15%;
		}

		.content {
			.show-name {
				display: flex;
				width: 100%;
				justify-content: space-evenly;
				align-items: center;
				margin-top: 9%;

				.text {
					width: 128px;
					text-align: right;
				}

				.input {
					width: 72%;
				}
			}
		}

		.button-box {
			.delete-button {
				position: absolute;
				right: 3%;
			}
		}
	}
}
</style>
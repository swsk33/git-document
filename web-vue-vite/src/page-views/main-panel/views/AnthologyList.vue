<template>
	<div class="anthology-panel" v-loading="!loadingDone" element-loading-text="正在拉取文集列表...">
		<div class="top-box">
			<div class="title">文集列表</div>
			<el-switch v-model="showStarOnly" class="show-star-only-switch" size="large" inline-prompt active-text="仅显示收藏" inactive-text="显示全部"/>
			<el-button type="primary" plain class="add" v-if="userStore.hasPermission('edit_anthology')" @click="addAnthologyRef.frameShow = true">增加文集</el-button>
		</div>
		<!-- 文集列表 -->
		<ul class="anthology-list">
			<!-- 每个文集 -->
			<li v-for="item in list" :key="item.id" v-show="!showStarOnly || (showStarOnly && containStar(item.id))">
				<div class="image">
					<img :src="parseCoverURL(item.cover)" alt="undefined"/>
				</div>
				<el-tooltip placement="top" :content="item.showName">
					<div class="text">{{ item.showName }}</div>
				</el-tooltip>
				<el-tooltip placement="top" content="最后更新时间">
					<div class="update-time">{{ getUpdateTime(item.updateTime) }}</div>
				</el-tooltip>
				<div class="button-box">
					<el-button type="info" plain class="star" :icon="containStar(item.id) ? StarFilled : Star" @click="containStar(item.id) ? cancelStar(item.id) : doStar(item.id)" circle/>
					<el-button type="primary" plain class="copy-ssh" :id="'copy-ssh-' + item.id" v-if="userStore.hasPermission('edit_anthology')" @click="copySSH(item.id, item.repoPath)">复制Git SSH地址</el-button>
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
					<el-input class="input" size="large" v-model="addAnthologyInfo.name" placeholder="请输入文集名，由英文和数字组成"/>
				</div>
				<div class="show-name">
					<div class="text">文集显示名：</div>
					<el-input class="input" size="large" v-model="addAnthologyInfo.showName" placeholder="请输入文集显示名"/>
				</div>
			</template>
			<template v-slot:button-box>
				<el-button class="ok" size="large" type="success" @click="addAnthology">确定</el-button>
				<el-button class="cancel" size="large" type="warning" @click="addAnthologyRef.frameShow = false">取消</el-button>
			</template>
		</InfoDialog>
		<!-- 信息弹窗 - 修改文集 -->
		<InfoDialog class="edit-anthology" ref="editAnthologyRef">
			<template v-slot:title>修改文集</template>
			<template v-slot:content>
				<upload-image class="cover" :default-image="parseCoverURL(null)" :init-image="editAnthologyInfo.cover" upload-name="image" ref="uploadImage">
					<template v-slot:text>上传封面：</template>
				</upload-image>
				<div class="show-name">
					<div class="text">文集显示名：</div>
					<el-input class="input" size="large" v-model="editAnthologyInfo.showName" placeholder="请输入文集显示名"/>
				</div>
			</template>
			<template v-slot:button-box>
				<el-button class="ok" type="success" size="large" @click="editAnthology">确定</el-button>
				<el-button class="cancel" type="warning" size="large" @click="editAnthologyRef.frameShow = false">取消</el-button>
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
import { reactive, ref, computed, onBeforeMount, watch } from 'vue';
import { Star, StarFilled } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';
import { parseCoverURL, REQUEST_PREFIX } from '../../../param/request-prefix';

const router = useRouter();

// 组件
import InfoDialog from '../components/InfoDialog.vue';
import UploadImage from '../components/UploadImage.vue';

const addAnthologyRef = ref(null);
const editAnthologyRef = ref(null);
const uploadImage = ref(null);

// pinia
import { useUserStore } from '../../../store/user';
import { useMetaDataStore } from '../../../store/meta-data';
import { MESSAGE_TYPE, showNotification } from '../../../utils/message';

const userStore = useUserStore();
const metaStore = useMetaDataStore();

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
// 用户收藏的文集列表
const userStar = ref([]);
// 是否只显示收藏
const showStarOnly = ref(false);

/**
 * 将时间戳转换为标准时间
 */
const getUpdateTime = computed(() => (timestamp) => {
	return timestampToDateString(timestamp);
});

/**
 * 判断传入的文集id是否是当前用户收藏的文集
 * @param {number} anthologyId 文集id
 * @return {boolean} 是否是当前用户收藏的
 */
const containStar = computed(() => (anthologyId) => {
	for (let item of userStar.value) {
		if (item.anthology.id === anthologyId) {
			return true;
		}
	}
	return false;
});

/**
 * 获取文集列表
 */
async function getAnthologyList() {
	loadingDone.value = false;
	const response = await sendRequest(REQUEST_PREFIX.ANTHOLOGY + 'get-all', REQUEST_METHOD.GET);
	loadingDone.value = true;
	if (!response.success) {
		showNotification('错误', response.message, MESSAGE_TYPE.error);
		return;
	}
	list.value = response.data;
}

/**
 * 复制文集Git SSH克隆地址
 * @param id 传入文集id
 * @param repoPath 传入仓库路径
 */
function copySSH(id, repoPath) {
	const clipBoard = new ClipBoard('#copy-ssh-' + id, {
		text() {
			return metaStore.sshPort !== 22 ? 'ssh://' + metaStore.systemUser + '@' + location.hostname + ':' + metaStore.sshPort + repoPath : metaStore.systemUser + '@' + location.hostname + ':' + repoPath;
		}
	});
	clipBoard.on('success', () => {
		showNotification('成功', '复制成功！');
		clipBoard.destroy();
	});
}

/**
 * 添加文集
 */
async function addAnthology() {
	const response = await sendRequest(REQUEST_PREFIX.ANTHOLOGY + 'add', REQUEST_METHOD.POST, addAnthologyInfo);
	if (!response.success) {
		showNotification('错误', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
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
	// 修改信息之前，先上传图片
	editAnthologyInfo.cover = await uploadImage.value.uploadAndGetUrl();
	// 修改文集数据
	const response = await sendRequest(REQUEST_PREFIX.ANTHOLOGY + 'update', REQUEST_METHOD.PATCH, editAnthologyInfo);
	if (!response.success) {
		showNotification('失败', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
	editAnthologyRef.value.frameShow = false;
	// 刷新列表
	await getAnthologyList();
}

/**
 * 删除当前编辑的文集
 */
async function deleteAnthology() {
	const response = await sendRequest(REQUEST_PREFIX.ANTHOLOGY + 'delete/' + editAnthologyInfo.id, REQUEST_METHOD.DELETE);
	if (!response.success) {
		showNotification('失败', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
	editAnthologyRef.value.frameShow = false;
	// 刷新列表
	await getAnthologyList();
}

/**
 * 获取当前用户收藏的文集
 */
async function getUserStar() {
	const response = await sendRequest(REQUEST_PREFIX.STAR + 'get-by-user', REQUEST_METHOD.GET);
	if (response.success) {
		userStar.value = response.data;
	}
}

/**
 * 收藏文集
 * @param anthologyId 要收藏的文集id
 */
async function doStar(anthologyId) {
	const response = await sendRequest(REQUEST_PREFIX.STAR + 'add', REQUEST_METHOD.POST, {
		user: {
			id: userStore.userData.id
		},
		anthology: {
			id: anthologyId
		}
	});
	if (!response.success) {
		showNotification('失败', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
	// 刷新列表
	await getUserStar();
}

/**
 * 取消收藏文集
 * @param anthologyId 取消收藏的文集id
 */
async function cancelStar(anthologyId) {
	let starId;
	for (let item of userStar.value) {
		if (item.anthology.id === anthologyId) {
			starId = item.id;
			break;
		}
	}
	const response = await sendRequest(REQUEST_PREFIX.STAR + 'delete/' + starId, REQUEST_METHOD.DELETE);
	if (!response.success) {
		showNotification('失败', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
	// 刷新列表
	await getUserStar();
}

// 监听是否只显示收藏按钮，将结果存入本地缓存
watch(showStarOnly, (newValue) => {
	localStorage.setItem('show-star-only', JSON.stringify(newValue));
});

onBeforeMount(async () => {
	// 挂载组件时获取文集列表
	await getAnthologyList();
	await getUserStar();
	// 获取本地缓存中的显示全部/仅显示收藏选项
	let item = localStorage.getItem('show-star-only');
	if (item != null) {
		showStarOnly.value = JSON.parse(item);
	}
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

		.show-star-only-switch {
			position: relative;
			right: 2%;
			--el-switch-off-color: #008800;
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

	// 弹窗-增加文集
	.add-anthology {
		:deep(.title) {
			font-size: 32px;
			top: 4%;
		}

		.content {
			.name, .show-name {
				position: relative;
				display: flex;
				align-items: center;
				justify-content: space-evenly;
				width: 90%;

				.text {
					position: relative;
					width: 30%;
					text-align: center;
				}

				.input {
					position: relative;
					width: 60%;
				}
			}
		}

		.button-box {
			.ok, .cancel {
				font-size: 18px;
			}
		}
	}

	// 弹窗-修改文集
	.edit-anthology {
		:deep(.content) {
			width: 70%;
			left: 15%;
		}

		.content {
			.show-name {
				display: flex;
				width: 95%;
				height: 25%;
				justify-content: space-between;
				align-items: center;
				margin-top: 9%;

				.text {
					width: 30%;
					text-align: center;
				}

				.input {
					width: 65%;
					height: 70%;
				}
			}
		}

		.button-box {
			.ok, .cancel {
				font-size: 17px;
			}

			.delete-button {
				position: absolute;
				right: 8px;
				bottom: 8px;
			}
		}
	}
}
</style>
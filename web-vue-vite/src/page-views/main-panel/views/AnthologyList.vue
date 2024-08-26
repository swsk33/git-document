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
			<li v-for="item in list" :key="item.id" v-show="!showStarOnly || (showStarOnly && userStarMap.has(item.id))">
				<!-- 文集封面 -->
				<div class="image">
					<img :src="parseCoverURL(item.cover)" alt="undefined"/>
				</div>
				<!-- 文集显示名称 -->
				<el-tooltip placement="top" :content="item.showName">
					<div class="text">{{ item.showName }}</div>
				</el-tooltip>
				<!-- 文集状态 -->
				<el-tooltip placement="top" :content="ANTHOLOGY_STATUS.get(item.status).tip">
					<div class="status" :style="{ color: ANTHOLOGY_STATUS.get(item.status).tipColor }">{{ ANTHOLOGY_STATUS.get(item.status).showName }}</div>
				</el-tooltip>
				<!-- 文集更新时间 -->
				<el-tooltip placement="top" content="最后更新时间">
					<div class="update-time">{{ getUpdateTime(item.updateTime) }}</div>
				</el-tooltip>
				<!-- 按钮盒子 -->
				<div class="button-box">
					<div class="star-box">
						<el-button type="info" plain class="star" :icon="userStarMap.has(item.id) ? StarFilled : Star" @click="userStarMap.has(item.id) ? cancelStar(item.id) : doStar(item.id)" circle/>
						<el-tooltip placement="top" :content="'收藏数：' + item.starCount">
							<div class="count">{{ item.starCount }}</div>
						</el-tooltip>
					</div>
					<el-button type="primary" plain class="copy-ssh" :id="'copy-ssh-' + item.id" v-if="userStore.hasPermission('edit_anthology') && item.status !== 'ARCHIVE'" @click="copySSH(item.id, item.repoPath)">复制Git SSH地址</el-button>
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
					<el-input class="input" size="large" v-model="addAnthologyObject.name" placeholder="请输入文集名，由英文和数字组成"/>
				</div>
				<div class="show-name">
					<div class="text">文集显示名：</div>
					<el-input class="input" size="large" v-model="addAnthologyObject.showName" placeholder="请输入文集显示名"/>
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
				<upload-image class="cover" :default-image="parseCoverURL(null)" :init-image="editAnthologyObject.cover" upload-name="image" ref="uploadImage">
					<template v-slot:text>上传封面：</template>
				</upload-image>
				<div class="show-name">
					<div class="text">文集显示名：</div>
					<el-input class="input" size="large" v-model="editAnthologyObject.showName" placeholder="请输入文集显示名"/>
				</div>
				<div class="status">
					<div class="text">文集状态：</div>
					<el-select class="select" v-model="editAnthologyObject.status" size="large">
						<el-option v-for="item in ANTHOLOGY_STATUS.values()" :key="item.name" :label="item.showName" :value="item.name"/>
					</el-select>
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
import { reactive, ref, computed, onBeforeMount, watch } from 'vue';
import { useRouter } from 'vue-router';
import { Star, StarFilled } from '@element-plus/icons-vue';
import { timestampToDateString } from '../../../utils/time-convert.js';
import { MESSAGE_TYPE, showNotification } from '../../../utils/message.js';
import { ANTHOLOGY_STATUS } from '../../../param/anthology-status.js';

const router = useRouter();

// 组件
import InfoDialog from '../components/InfoDialog.vue';
import UploadImage from '../components/UploadImage.vue';

const addAnthologyRef = ref(null);
const editAnthologyRef = ref(null);
const uploadImage = ref(null);

// pinia
import { useUserStore } from '../../../store/user.js';
import { useMetaDataStore } from '../../../store/meta-data.js';
import { anthologyAdd, anthologyDelete, anthologyGetAll, anthologyUpdate } from '../../../api/anthology-api.js';
import { parseCoverURL } from '../../../api/image-api.js';
import { starAdd, starDelete, starGetCount } from '../../../api/star-api.js';
import { isEmpty } from '../../../utils/string.js';

const userStore = useUserStore();
const metaStore = useMetaDataStore();

/**
 * 所有文集列表
 */
const list = ref([]);

/**
 * 是否加载完成
 */
const loadingDone = ref(false);

/**
 * 当前要添加的文集信息
 */
const addAnthologyObject = reactive({
	name: undefined,
	showName: undefined
});

/**
 * 当前正在编辑的文集信息
 */
const editAnthologyObject = ref(undefined);

/**
 * 将时间戳转换为标准时间
 */
const getUpdateTime = computed(() => (timestamp) => {
	return timestampToDateString(timestamp);
});

/**
 * 获取文集列表
 */
async function getAnthologyList() {
	loadingDone.value = false;
	const response = await anthologyGetAll();
	loadingDone.value = true;
	if (!response.success) {
		showNotification('错误', response.message, MESSAGE_TYPE.error);
		return;
	}
	list.value = response.data;
}

/**
 * 添加文集
 */
async function addAnthology() {
	const response = await anthologyAdd(addAnthologyObject);
	if (!response.success) {
		showNotification('错误', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
	addAnthologyRef.value.frameShow = false;
	// 重新获取文集列表
	await getAnthologyList();
	await getStarCount();
}

/**
 * 显示修改文集弹窗
 * @param anthology 文集对象
 */
function showEditDialog(anthology) {
	editAnthologyObject.value = anthology;
	editAnthologyRef.value.frameShow = true;
}

/**
 * 修改文集信息
 */
async function editAnthology() {
	// 修改信息之前，先上传图片
	editAnthologyObject.value.cover = await uploadImage.value.uploadAndGetUrl();
	// 修改文集数据
	const response = await anthologyUpdate(editAnthologyObject.value);
	if (!response.success) {
		showNotification('失败', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
	editAnthologyRef.value.frameShow = false;
	// 刷新列表
	await getAnthologyList();
	await getStarCount();
}

/**
 * 删除当前编辑的文集
 */
async function deleteAnthology() {
	const response = await anthologyDelete(editAnthologyObject.value.id);
	if (!response.success) {
		showNotification('失败', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
	editAnthologyRef.value.frameShow = false;
	// 刷新列表
	await getAnthologyList();
	await getStarCount();
}

/**
 * 复制文集Git SSH克隆地址
 * @param id 传入文集id
 * @param repoPath 传入仓库路径
 */
function copySSH(id, repoPath) {
	const clipBoard = new ClipBoard('#copy-ssh-' + id, {
		text() {
			return metaStore.sshPort !== 22 ? `ssh://${metaStore.systemUser}@${location.hostname}:${metaStore.sshPort}${repoPath}` : `${metaStore.systemUser}@${location.hostname}:${repoPath}`;
		}
	});
	clipBoard.on('success', () => {
		showNotification('成功', '复制成功！');
		clipBoard.destroy();
	});
}

/**
 * 用户收藏的文集id对应收藏id的哈希表<br>
 * - 键：被收藏的文集id
 * - 值：收藏对象id
 */
const userStarMap = ref(new Map());

/**
 * 是否只显示收藏
 */
const showStarOnly = ref(false);

/**
 * 读取用户信息，初始化用户收藏的文集id集合
 */
const initializeStarMap = () => {
	const map = userStarMap.value;
	map.clear();
	for (let star of userStore.userData.stars) {
		map.set(star.anthologyId, star.id);
	}
};

/**
 * 收藏文集
 * @param anthologyId 要收藏的文集id
 */
async function doStar(anthologyId) {
	const response = await starAdd(anthologyId);
	if (!response.success) {
		showNotification('失败', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
	// 同步更改到收藏哈希表
	userStarMap.value.set(response.data.anthologyId, response.data.id);
	await getStarCount(anthologyId);
}

/**
 * 取消收藏文集
 * @param anthologyId 取消收藏的文集id
 */
async function cancelStar(anthologyId) {
	const response = await starDelete(userStarMap.value.get(anthologyId));
	if (!response.success) {
		showNotification('失败', response.message, MESSAGE_TYPE.error);
		return;
	}
	showNotification('成功', response.message);
	// 同步更改到收藏哈希表
	userStarMap.value.delete(anthologyId);
	await getStarCount(anthologyId);
}

/**
 * 获取并设定所有文集或者对应文集的收藏数
 * @param {String} anthologyId 要获取收藏数的文集id，当该参数省略时，则获取全部文集的收藏数
 */
async function getStarCount(anthologyId = null) {
	// 指定id时只获取对应id的文集收藏数
	if (!isEmpty(anthologyId)) {
		const response = await starGetCount(anthologyId);
		if (!response.success) {
			return;
		}
		for (let item of list.value) {
			item.starCount = response.data;
		}
		return;
	}
	// 未指定id时，获取全部文章收藏数
	for (let item of list.value) {
		item.starCount = 0;
		const response = await starGetCount(item.id);
		if (!response.success) {
			continue;
		}
		item.starCount = response.data;
	}
}

// 监听是否只显示收藏按钮，将结果存入本地缓存
watch(showStarOnly, (newValue) => {
	localStorage.setItem('show-star-only', JSON.stringify(newValue));
});

onBeforeMount(async () => {
	// 挂载组件时获取文集列表
	await getAnthologyList();
	// 加载用户收藏信息
	initializeStarMap();
	await getStarCount();
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

	// 文集列表（每一个列表项）
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
			user-select: none;

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
				margin-left: 2%;
				width: 27%;
				font-size: 18px;
				padding-left: 3px;
			}

			.status {
				font-size: 15px;
				line-height: 18px;
				height: 18px;
				width: 4%;
				border-radius: 5px;
				border-style: solid;
				border-width: 1px;
				padding: 2px 4px;
				margin-left: 1%;
				text-align: center;
				box-sizing: content-box;
				text-overflow: ellipsis;
				overflow: hidden;
				white-space: nowrap;
			}

			.update-time {
				width: 18%;
				font-size: 13px;
				text-align: center;
				color: #5500ff;
				margin-left: 1%;
			}

			.button-box {
				display: flex;
				justify-content: space-evenly;
				align-items: center;
				width: 36%;
				height: 6vh;
				margin-left: 2%;

				.star-box {
					display: flex;
					justify-content: space-evenly;
					align-items: center;
					width: 17%;

					.count {
						position: relative;
						text-overflow: ellipsis;
						white-space: nowrap;
						overflow: hidden;
						color: #7b00db;
						font-family: Arial, serif;
					}
				}
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
			.cover {
				margin-bottom: 5%;
			}

			.show-name, .status {
				display: flex;
				width: 95%;
				height: 25%;
				align-items: center;

				.text {
					width: 30%;
					text-align: center;
				}
			}

			.show-name {
				.input {
					width: 70%;
					height: 70%;
				}
			}

			.status {
				.select {
					width: 30%;
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
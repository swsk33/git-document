<template>
	<div class="main-panel">
		<TopBar ref="topBar"/>
		<SideMenu ref="sideMenu" @closeAvatarMenu="closeAvatarMenu"/>
		<router-view ref="controlPanel" @click="closeAvatarMenu" class="panel-content"/>
	</div>
</template>

<script setup>
import { ref, watch } from 'vue';

// 组件及其ref
import TopBar from './components/TopBar.vue';
import SideMenu from './components/SideMenu.vue';

const topBar = ref(null);
const sideMenu = ref(null);
const controlPanel = ref(null);

// pinia
import { useMetaDataStore } from '../../store/meta-data';

const metaStore = useMetaDataStore();

/**
 * 关闭头像菜单
 */
function closeAvatarMenu() {
	topBar.value.menuControl(false);
}

// 监听标题
watch(() => metaStore.organizationName, () => {
	metaStore.setTitle('GitDocument');
}, {
	immediate: true
});
</script>

<style lang="scss" scoped>
.main-panel {
	position: absolute;
	width: 100%;
	height: 100vh;
	background: url("/api/config-get/background") no-repeat center/cover;

	.panel-content {
		position: absolute;
		left: 18vw;
		top: 8vh;
		width: 80vw;
		height: 90vh;
		background-color: rgba(255, 255, 255, 0.45);
		border-radius: 6px;
		overflow-y: scroll;

		// 设定滚动条整体
		&::-webkit-scrollbar {
			width: 5px;
			height: 5px;
		}

		// 设定滚动条滑块
		&::-webkit-scrollbar-thumb {
			border-radius: 10px;
			background: rgba(0, 0, 0, 0.2);
		}

		// 设定外层轨道滚动槽
		&::-webkit-scrollbar-track {
			border-radius: 5px;
			background: rgba(0, 0, 0, 0.1);
		}
	}
}
</style>
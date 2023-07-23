<template>
	<!-- 顶栏 -->
	<div class="top-bar">
		<!-- 手机模式显示菜单 -->
		<el-button class="show-menu-button" @click="themeStore.setMenuShow(true)" :icon="Memo" type="primary" plain circle/>
		<!-- 切换文章按钮及其树形组件 -->
		<div class="switch-article">
			<el-button class="button" :icon="Document" type="success" plain @click="themeStore.articleSwitchShow = true"/>
			<div class="tree-container" v-show="themeStore.articleSwitchShow">
				<div class="text">点击目录树以切换文章</div>
				<el-tree class="tree" v-if="articleTreeStore.currentArticleTree != null" :data="articleTreeStore.currentArticleTree.children" :props="treeProperties" accordion @node-click="switchArticle"/>
			</div>
		</div>
		<!-- 更改颜色主题按钮盒子 -->
		<div class="color-box">
			<div class="text">主题</div>
			<el-tooltip placement="bottom" content="切换为粉色">
				<div class="color-button pink-button" @click="changePageColor('pink')">
					<Check class="checked" v-if="themeStore.pageColor.pink"/>
				</div>
			</el-tooltip>
			<el-tooltip placement="bottom" content="切换为蓝色">
				<div class="color-button blue-button" @click="changePageColor('blue')">
					<Check class="checked" v-if="themeStore.pageColor.blue"/>
				</div>
			</el-tooltip>
			<el-tooltip placement="bottom" content="切换为橙色">
				<div class="color-button orange-button" @click="changePageColor('orange')">
					<Check class="checked" v-if="themeStore.pageColor.orange"/>
				</div>
			</el-tooltip>
			<el-tooltip placement="bottom" content="切换为绿色">
				<div class="color-button green-button" @click="changePageColor('green')">
					<Check class="checked" v-if="themeStore.pageColor.green"/>
				</div>
			</el-tooltip>
			<el-tooltip placement="bottom" content="切换为灰色">
				<div class="color-button gray-button" @click="changePageColor('gray')">
					<Check class="checked" v-if="themeStore.pageColor.gray"/>
				</div>
			</el-tooltip>
		</div>
		<!-- 夜晚模式按钮 -->
		<el-switch class="switch-theme" v-model="themeStore.isNight" :active-icon="elementIcon.moon" :inactive-icon="elementIcon.sunny"/>
	</div>
</template>

<script setup>
import { MESSAGE_TYPE, showNotification } from '../../../utils/message';
import { reactive, shallowRef, watch } from 'vue';
import { Check, Memo, Moon, Sunny, Document } from '@element-plus/icons-vue';

// pinia
import { useArticlePageThemeStore } from '../../../store/article-page-theme';
import { useArticleTreeStore } from '../../../store/article-tree';

const themeStore = useArticlePageThemeStore();
const articleTreeStore = useArticleTreeStore();

/**
 * 图标
 */
const elementIcon = reactive({
	moon: shallowRef(Moon),
	sunny: shallowRef(Sunny)
});

/**
 * 树形组件的对象属性
 */
const treeProperties = {
	children: 'children',
	label: 'label',
	class: 'tree-node'
};

/**
 * 点击树形组件时切换文章
 * @param nodeData 树节点所绑定的节点数据对象
 */
function switchArticle(nodeData) {
	if (nodeData.id != null) {
		location.pathname = '/article/' + nodeData.id;
	}
}

/**
 * 改变页面颜色主题
 */
function changePageColor(color) {
	themeStore.setPageColor(color);
	showNotification('成功', '更换主题成功！', MESSAGE_TYPE.success, 750);
}

// 监听器
watch(() => themeStore.isNight, () => {
	localStorage.setItem('night', JSON.stringify(themeStore.isNight));
});
</script>

<style lang="scss">
.top-bar {
	display: flex;
	justify-content: flex-end;
	align-items: center;
	top: 0;
	width: 100%;
	height: 7vh;
	border-bottom: 1px solid #eaecef;
	box-sizing: border-box;

	.show-menu-button {
		position: absolute;
		display: none;
		left: 1.5vh;
		width: 4vh;
		height: 4vh;
	}

	.switch-article {
		position: absolute;
		width: 3.5vh;
		height: 3.5vh;
		left: 17vw;
		top: 1.5vh;

		.tree-container {
			position: absolute;
			width: 50vw;
			height: 35vh;
			z-index: 9;
			overflow: scroll;
			background-color: white;
			user-select: none;

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
				border-radius: 0;
				background: rgba(0, 0, 0, 0.1);
			}

			.text {
				position: relative;
				font-size: 18px;
				padding: 4px 20px;
				height: 24px;
				line-height: 24px;
				border-bottom-width: 1px;
				border-bottom-style: solid;
				margin-bottom: 2px;
			}

			.tree {
				position: relative;

				.tree-node > .el-tree-node__content {
					font-size: 16px;
					height: 36px;
					line-height: 36px;
					background-color: #00000000;
				}
			}
		}

		.button {
			width: 4vh;
			height: 4vh;
		}
	}

	.color-box {
		position: relative;
		display: flex;
		justify-content: space-evenly;
		align-items: center;
		height: 60%;
		width: 235px;
		margin-right: 2%;

		.color-button {
			position: relative;
			width: 22px;
			height: 22px;
			border-radius: 50%;
		}

		.checked {
			position: absolute;
			width: 16px;
			left: 3px;
			top: 3px;
		}

		.pink-button {
			background-color: #ffabfe;
		}

		.blue-button {
			background-color: #76adff;
		}

		.orange-button {
			background-color: #ffbc47;
		}

		.green-button {
			background-color: #82ff8b;
		}

		.gray-button {
			background-color: #c4c4c4;
		}
	}

	.switch-theme {
		position: relative;
		margin-right: 3%;
	}
}
</style>
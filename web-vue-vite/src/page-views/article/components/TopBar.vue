<template>
	<!-- 顶栏 -->
	<div class="top-bar">
		<!-- 手机模式显示菜单 -->
		<div class="show-menu-button" @click="themeStore.setMenuShow(true)">
			<div></div>
			<div></div>
			<div></div>
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
import { ElNotification } from 'element-plus';
import { reactive, shallowRef, watch } from 'vue';

// element-plus图标
import { Check, Moon, Sunny } from '@element-plus/icons-vue';

// pinia
import { useArticlePageThemeStore } from '../../../store/article-page-theme';

const themeStore = useArticlePageThemeStore();

// 自定义响应式变量
const elementIcon = reactive({
	moon: shallowRef(Moon),
	sunny: shallowRef(Sunny)
});

// 自定义方法

/**
 * 改变页面颜色主题
 */
function changePageColor(color) {
	themeStore.setPageColor(color);
	ElNotification({
		title: '成功！',
		message: '更换主题成功！',
		type: 'success',
		position: 'top-left',
		duration: 750
	});
}

// 监听器
watch(() => themeStore.isNight, () => {
	localStorage.setItem('night', JSON.stringify(themeStore.isNight));
});
</script>

<style lang="scss">
.top-bar {
	display: flex;
	align-items: center;
	justify-content: flex-end;
	top: 0;
	width: 100%;
	height: 7vh;
	border-bottom: 1px solid #eaecef;
	box-sizing: border-box;

	.show-menu-button {
		position: absolute;
		display: none;
		flex-direction: column;
		justify-content: space-evenly;
		align-items: center;
		left: 1.25vh;
		width: 4.5vh;
		height: 4.5vh;
		border-radius: 4px;
		border: gray 1px solid;

		div {
			width: 75%;
			height: 16%;
			background-color: gray;
			border-radius: 3px;
		}
	}

	.color-box {
		position: relative;
		display: flex;
		justify-content: space-evenly;
		align-items: center;
		margin-right: 24px;
		height: 60%;
		width: 235px;

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
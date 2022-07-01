<template>
	<!-- 顶栏 -->
	<div class="top-bar">
		<!-- 手机模式显示菜单 -->
		<div class="show-menu-button" @click="setMenuShow(true)">
			<div></div>
			<div></div>
			<div></div>
		</div>
		<!-- 更改颜色主题按钮盒子 -->
		<div class="color-box">
			<div class="text">主题</div>
			<el-tooltip placement="bottom" content="切换为粉色">
				<div class="color-button pink-button" @click="changePageColor('pink')">
					<check class="checked" v-if="pageColor.pink"/>
				</div>
			</el-tooltip>
			<el-tooltip placement="bottom" content="切换为蓝色">
				<div class="color-button blue-button" @click="changePageColor('blue')">
					<check class="checked" v-if="pageColor.blue"/>
				</div>
			</el-tooltip>
			<el-tooltip placement="bottom" content="切换为橙色">
				<div class="color-button orange-button" @click="changePageColor('orange')">
					<check class="checked" v-if="pageColor.orange"/>
				</div>
			</el-tooltip>
			<el-tooltip placement="bottom" content="切换为绿色">
				<div class="color-button green-button" @click="changePageColor('green')">
					<check class="checked" v-if="pageColor.green"/>
				</div>
			</el-tooltip>
			<el-tooltip placement="bottom" content="切换为灰色">
				<div class="color-button gray-button" @click="changePageColor('gray')">
					<check class="checked" v-if="pageColor.gray"/>
				</div>
			</el-tooltip>
		</div>
		<!-- 夜晚模式按钮 -->
		<el-switch class="switch-theme" v-model="night" :active-icon="elementIcon.moon" :inactive-icon="elementIcon.sunny"/>
	</div>
</template>

<script>
import { ElNotification } from 'element-plus';
import { shallowRef } from 'vue';
import { createNamespacedHelpers } from 'vuex';

// element-plus图标
import { Check, Moon, Sunny } from '@element-plus/icons-vue';

// vuex模块
const { mapState: themeState, mapActions: themeActions } = createNamespacedHelpers('article-page-theme');

export default {
	components: {
		check: Check
	},
	data() {
		return {
			// 是否为夜晚（按钮绑定值）
			night: false,
			// 引入的图标
			elementIcon: {
				moon: shallowRef(Moon),
				sunny: shallowRef(Sunny)
			}
		};
	},
	computed: {
		...themeState(['isNight', 'pageColor'])
	},
	methods: {
		...themeActions(['setIsNight', 'setMenuShow', 'setPageColor']),
		changePageColor(color) {
			this.setPageColor(color);
			ElNotification({
				title: '成功！',
				message: '更换主题成功！',
				type: 'success',
				position: 'top-left',
				duration: 750
			});
		}
	},
	watch: {
		night() {
			this.setIsNight(this.night);
		}
	}
};
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
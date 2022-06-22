<template>
	<!-- 手机模式显示菜单 -->
	<div class="show-menu-button" @click="showMenuButton">
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
	<el-switch class="switch-theme" v-model="isNight" :active-icon="elementIcon.moon" :inactive-icon="elementIcon.sunny"/>
</template>

<script>
// element-plus图标
import { Check, Moon, Sunny } from '@element-plus/icons-vue';
import { shallowRef } from 'vue';
import { ElNotification } from 'element-plus';

export default {
	components: {
		check: Check
	},
	data() {
		return {
			// 页面颜色主题
			pageColor: {
				pink: true,
				blue: false,
				green: false,
				orange: false,
				gray: false
			},
			// 引入的图标
			elementIcon: {
				moon: shallowRef(Moon),
				sunny: shallowRef(Sunny)
			}
		};
	},
	methods: {
		/**
		 * 更改页面颜色主题
		 * @param color 颜色字符串
		 * @param showTip 是否显示提示
		 */
		changePageColor(color, showTip = true) {
			for (let key in this.pageColor) {
				this.pageColor[key] = false;
			}
			switch (color) {
				case 'pink':
					this.pageColor.pink = true;
					break;
				case 'blue':
					this.pageColor.blue = true;
					break;
				case 'green':
					this.pageColor.green = true;
					break;
				case 'orange':
					this.pageColor.orange = true;
					break;
				case 'gray':
					this.pageColor.gray = true;
					break;
				default:
					this.pageColor.pink = true;
					break;
			}
			if (showTip) {
				ElNotification({
					title: '成功！',
					message: '切换主题完成！',
					type: 'success',
					position: 'top-left',
					duration: 1000
				});
			}
			// 存放设定到本地缓存
			window.localStorage.setItem('color', color);
		},
		/**
		 * 手机端显示菜单按钮
		 */
		showMenuButton() {
			this.menuShow = !this.menuShow;
		},
	}
};
</script>

<style scoped>

</style>
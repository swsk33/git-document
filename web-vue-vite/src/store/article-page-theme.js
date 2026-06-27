// 页面主题
import { defineStore } from 'pinia';

export const useArticlePageThemeStore = defineStore('article-page-theme', {
	state() {
		return {
			/**
			 * 记录网页是否被解析完成
			 */
			contentParsed: false,
			/**
			 * 是否显示菜单（手机端）
			 */
			menuShow: true,
			/**
			 * 是否是夜晚样式
			 */
			isNight: false,
			/**
			 * 是否是手机端
			 */
			isMobile: false,
			/**
			 * 页面颜色主题
			 */
			pageColor: {
				pink: true,
				blue: false,
				green: false,
				orange: false,
				gray: false
			}
		};
	},
	actions: {
		/**
		 * 设定菜单是否显示
		 * @param {boolean} show 布尔值，表示菜单是否显示
		 */
		setMenuShow(show) {
			if (!this.isMobile && !show) {
				return;
			}
			this.menuShow = show;
		},
		/**
		 * 设定页面主题颜色
		 * @param color 字符串，表示颜色值
		 */
		setPageColor(color) {
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
			// 存放设定到本地缓存
			window.localStorage.setItem('color', color);
		}
	}
});
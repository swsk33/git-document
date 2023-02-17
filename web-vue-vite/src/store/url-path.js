// 用于记录用户第一次访问但是被拦截回登录页的路径
import { defineStore } from 'pinia';

export const useUrlPathStore = defineStore('url-path', {
	state() {
		return {
			/**
			 * 用户访问网站时的路径
			 */
			path: '/'
		};
	},
	actions: {
		/**
		 * 设定路径
		 * @param path 设定的路径
		 */
		setPath(path) {
			this.path = path;
		}
	}
});
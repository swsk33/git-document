import { sendRequest, REQUEST_METHOD } from '../utils/request';
import { defineStore } from 'pinia';

export const useMetaDataStore = defineStore('meta-data', {
	state() {
		return {
			/**
			 * 组织名，先从本地缓存拿取
			 */
			organizationName: '获取中...',
			/**
			 * 是否允许公开注册
			 */
			allowPublic: false
		};
	},
	actions: {
		/**
		 * 从后端获取组织名
		 */
		async requestName() {
			const response = await sendRequest('/api/config-get/organization', REQUEST_METHOD.GET);
			if (response.success) {
				this.organizationName = response.data;
			}
		},
		/**
		 * 从后端获取是否允许公开注册
		 */
		async requestAllowPublic() {
			const response = await sendRequest('/api/config-get/allow-public', REQUEST_METHOD.GET);
			if (response.success) {
				this.allowPublic = response.data;
			}
		},
		/**
		 * 从后端获取全部元数据信息
		 */
		async getAllMeta() {
			await this.requestName();
			await this.requestAllowPublic();
		},
		/**
		 * 设置当前页的标签标题
		 * @param title 传入标题
		 */
		setTitle(title) {
			document.title = this.organizationName + ' | ' + title;
		}
	}
});
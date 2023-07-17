// 用户
import { sendRequest, REQUEST_METHOD } from '../utils/request';
import { defineStore } from 'pinia';
import { REQUEST_PREFIX } from '../param/request-prefix';

export const useUserStore = defineStore('user', {
	state() {
		return {
			/**
			 * 用户数据
			 */
			userData: undefined,
			/**
			 * 用户角色列表
			 */
			roleList: []
		};
	},
	actions: {
		/**
		 * 检查用户是否登录并拉取用户信息，若已登录，则将用户数据保存到state，否则置为undefined
		 */
		async checkLogin() {
			let response = await sendRequest(REQUEST_PREFIX.USER + 'is-login', REQUEST_METHOD.GET);
			// 若登录，则设定用户数据
			if (response.success) {
				this.userData = response.data;
			}
			return response.success;
		},
		/**
		 * 获取全角色列表
		 */
		async getRoleList() {
			let response = await sendRequest(REQUEST_PREFIX.ROLE + 'get-all', REQUEST_METHOD.GET);
			if (response.success) {
				this.roleList = response.data;
			}
		},
		/**
		 * 判断当前用户是否有权限
		 */
		hasPermission(permission) {
			if (this.userData === undefined) {
				return false;
			}
			const permissions = this.userData.role.permissions;
			for (let item of permissions) {
				if (permission === item.name) {
					return true;
				}
			}
			return false;
		}
	}
});
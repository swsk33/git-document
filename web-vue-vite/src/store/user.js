// 用户
import { defineStore } from 'pinia';
import { userIsLogin } from '../api/user-api.js';
import { roleGetAll } from '../api/role-api.js';

export const useUserStore = defineStore('user', {
	state() {
		return {
			/**
			 * 用户数据
			 * @type User
			 */
			userData: undefined,
			/**
			 * 全部用户角色列表
			 * @type Array<Role>
			 */
			roleList: []
		};
	},
	actions: {
		/**
		 * 检查用户是否登录并拉取用户信息，若已登录，则将用户数据保存到state，否则置为undefined
		 */
		async checkLogin() {
			let response = await userIsLogin();
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
			let response = await roleGetAll();
			if (response.success) {
				this.roleList = response.data;
			}
		},
		/**
		 * 判断当前用户是否有权限
		 */
		hasPermission(permission) {
			if (this.userData == null) {
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
// 用户
import { defineStore } from 'pinia';
import { userIsLogin } from '../api/user-api.js';
import { roleGetAll } from '../api/role-api.js';
import { starGetByLoginUser } from '../api/star-api.js';
import { publicKeyGetByLoginUser } from '../api/public-key-api.js';

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
			roleList: [],
			/**
			 * 用户的文集收藏列表<br>
			 * - 键：被收藏的文集id
			 * - 值：收藏对象的id
			 */
			starMap: new Map()
		};
	},
	actions: {
		/**
		 * 检查用户是否登录并拉取用户信息，若已登录，则将用户数据保存到state，否则置为undefined
		 */
		async checkLogin() {
			const response = await userIsLogin();
			// 若登录，则设定用户数据及其收藏列表
			if (response.success) {
				this.userData = response.data;
				this.starMap.clear();
				for (let star of this.userData.stars) {
					this.starMap.set(star.anthologyId, star.id);
				}
			}
			return response.success;
		},
		/**
		 * 刷新当前登录用户的收藏列表
		 */
		async refreshUserStar() {
			const response = await starGetByLoginUser();
			if (response.success) {
				this.userData.stars = response.data;
				this.starMap.clear();
				for (let star of this.userData.stars) {
					this.starMap.set(star.anthologyId, star.id);
				}
			}
			return response.success;
		},
		/**
		 * 刷新当前用户的公钥列表
		 */
		async refreshUserPublicKey() {
			const response = await publicKeyGetByLoginUser();
			if (response.success) {
				this.userData.keys = response.data;
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
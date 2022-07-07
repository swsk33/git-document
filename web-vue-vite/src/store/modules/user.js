// 用户
import { sendRequest, REQUEST_METHOD } from '../../utils/request.js';

export default {
	namespaced: true,
	state: {
		userData: undefined,
		isLogin: false,
		roleList: []
	},
	mutations: {
		/**
		 * 设定用户数据
		 * @param state 数据
		 * @param payload 用户对象
		 */
		setUserData(state, payload) {
			state.userData = payload;
		},
		/**
		 * 设定状态：是否登录
		 * @param state 数据
		 * @param payload 布尔值，表示是否登录
		 */
		setIsLogin(state, payload) {
			state.isLogin = payload;
		},
		/**
		 * 设定角色列表
		 * @param state 数据
		 * @param payload 角色对象数组
		 */
		setRoleList(state, payload) {
			state.roleList = payload;
		}
	},
	actions: {
		/**
		 * 检查用户是否登录，若已登录，则将用户数据保存到state，否则置为undefined
		 * @param context 上下文
		 */
		async checkLogin(context) {
			let response = await sendRequest('/api/user/is-login', REQUEST_METHOD.GET);
			// 若登录，则设定用户数据
			if (response.success) {
				context.commit('setUserData', response.data);
			} else {
				context.commit('setUserData', undefined);
			}
			context.commit('setIsLogin', response.success);
			return response.success;
		},
		/**
		 * 获取全角色列表
		 * @param context 上下文
		 */
		async getRoleList(context) {
			let response = await sendRequest('/api/role/get-all', REQUEST_METHOD.GET);
			if (response.success) {
				context.commit('setRoleList', response.data);
			}
		}
	},
	getters: {
		/**
		 * 判断当前用户是否有权限
		 * @param state 数据
		 * @returns {(function(*): (boolean))|*} 传入权限字符串并返回是否有权限
		 */
		hasPermission: (state) => (permission) => {
			if (state.userData === undefined) {
				return false;
			}
			const permissions = state.userData.role.permissions;
			for (let item of permissions) {
				if (permission === item.name) {
					return true;
				}
			}
			return false;
		},
		/**
		 * 获取用户昵称
		 * @param state 数据
		 * @returns {undefined|*} 用户昵称
		 */
		userNickname(state) {
			if (state.userData === undefined) {
				return undefined;
			}
			return state.userData.nickname;
		},
		/**
		 * 获取用户角色
		 * @param state 数据
		 * @return {undefined|string} 用户角色名
		 */
		userRole(state) {
			if (state.userData === undefined) {
				return undefined;
			}
			return state.userData.role.showName;
		},
		/**
		 * 获取用户头像
		 * @param state 数据
		 * @return {undefined|string} 用户头像url
		 */
		userAvatar(state) {
			if (state.userData === undefined) {
				return undefined;
			}
			return state.userData.avatar;
		},
		/**
		 * 获取角色列表
		 * @param state 数据
		 */
		roleList(state) {
			return state.roleList;
		},
		/**
		 * 是否登录
		 * @param state 数据
		 */
		isLogin(state) {
			return state.isLogin;
		}
	}
};
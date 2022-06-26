// 用户
import { sendRequest, REQUEST_METHOD } from '../../utils/request.js';
import br from '../../../dist/assets/MainBody.bddc26b9';

export default {
	namespaced: true,
	state: {
		userData: undefined
	},
	mutations: {
		/**
		 * 设定用户数据
		 * @param state 数据
		 * @param payload 用户对象
		 */
		setUserData(state, payload) {
			state.userData = payload;
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
			return response.success;
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
		getNickname(state) {
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
		getRole(state) {
			if (state.userData === undefined) {
				return undefined;
			}
			switch (state.userData.role.id) {
				case 1:
					return '管理员';
				case 2:
					return '团队成员';
				default:
					return undefined;
			}
		},
		/**
		 * 获取用户头像
		 * @param state 数据
		 * @return {undefined|string} 用户头像url
		 */
		getAvatar(state) {
			if (state.userData === undefined) {
				return undefined;
			}
			return state.userData.avatar;
		}
	}
};
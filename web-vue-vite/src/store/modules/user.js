// 用户
import { sendRequest, REQUEST_METHOD } from '../../utils/request.js';

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
		}
	}
};
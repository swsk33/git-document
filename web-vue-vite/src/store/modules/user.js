import { sendRequest, REQUEST_METHOD } from '../../utils/request.js';

export default {
	namespaced: true,
	state: {
		userData: undefined,
		isLogin: false
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
		 * 设定是否登录
		 * @param state 数据
		 * @param payload 布尔值，表示用户是否登录
		 */
		setLogin(state, payload) {
			state.isLogin = payload;
		}
	},
	actions: {
		/**
		 * 检查用户是否登录，若已登录，则将用户数据保存到state
		 * @param context 上下文
		 */
		async checkLogin(context) {
			let response = await sendRequest('/api/user/is-login', REQUEST_METHOD.GET);
			// 设定登录状态
			context.commit('setLogin', response.success);
			// 若登录，则设定用户数据
			if (context.state.isLogin) {
				context.commit('setUserData', response.data);
			}
		}
	}
};
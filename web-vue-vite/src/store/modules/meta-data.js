import { sendRequest, REQUEST_METHOD } from '../../utils/request.js';

export default {
	namespaced: true,
	state: {
		/**
		 * 组织名，先从本地缓存拿取
		 */
		organizationName: window.localStorage.getItem('org'),
		/**
		 * 是否允许公开注册
		 */
		allowPublic: false
	},
	mutations: {
		/**
		 * 设定组织名
		 * @param state 数据
		 * @param payload 传入组织名
		 */
		setName(state, payload) {
			state.name = payload;
		},
		/**
		 * 设定是否允许公开注册
		 * @param state 数据
		 * @param payload 传入是否允许公开注册
		 */
		setAllowPublic(state, payload) {
			state.allowPublic = payload;
		}
	},
	actions: {
		/**
		 * 从后端获取组织名，并存入本地缓存
		 * @param context 上下文
		 */
		async requestName(context) {
			const response = await sendRequest('/api/config-get/organization', REQUEST_METHOD.GET);
			if (!response.success) {
				return;
			}
			// 先和本地缓存比对
			if (context.state.name !== response.data) {
				window.localStorage.setItem('org', response.data);
				context.commit('setName', response.data);
			}
			// 否则直接使用本地缓存
		},
		/**
		 * 从后端获取是否允许公开注册
		 * @param context 上下文
		 */
		async requestAllowPublic(context) {
			const response = await sendRequest('/api/config-get/allow-public', REQUEST_METHOD.GET);
			if (response.success) {
				context.commit('setAllowPublic', response.data);
			}
		},
		/**
		 * 从后端获取全部元数据信息
		 * @param context 上下文
		 */
		async getAllMeta(context) {
			await context.dispatch('requestName');
			await context.dispatch('requestAllowPublic');
		}
	}
};
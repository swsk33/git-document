import { sendRequest, REQUEST_METHOD } from '../../utils/request.js';

export default {
	namespaced: true,
	state: {
		/**
		 * 组织名，先从本地缓存拿取
		 */
		organizationName: '获取中...',
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
			state.organizationName = payload;
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
		 * 从后端获取组织名
		 * @param context 上下文
		 */
		async requestName(context) {
			const response = await sendRequest('/api/config-get/organization', REQUEST_METHOD.GET);
			if (response.success) {
				context.commit('setName', response.data);
			}
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
		},
		/**
		 * 设置当前页的标签标题
		 * @param context 上下文
		 * @param payload 参数，标题后缀，标题最终值为：组织名 | 标题后缀
		 */
		setTitle(context, payload) {
			document.title = context.state.organizationName + ' | ' + payload;
		}
	}
};
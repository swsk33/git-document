import { sendRequest, REQUEST_METHOD } from '../../utils/request.js';
import { ref } from 'vue';

export default {
	namespaced: true,
	state: {
		/**
		 * 组织名
		 */
		name: undefined
	},
	mutations: {
		/**
		 * 设定组织名
		 * @param state 数据
		 * @param payload 传入组织名
		 */
		setName(state, payload) {
			state.name = payload;
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
		}
	},
	getters: {
		/**
		 * 取得组织名
		 * @param state 数据
		 * @return {*} 组织名
		 */
		organizationName(state) {
			return ref(state.name);
		}
	}
};
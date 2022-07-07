// 用于记录用户第一次访问但是被拦截回登录页的路径
export default {
	namespaced: true,
	state: {
		/**
		 * 用户访问网站时的路径
		 */
		path: '/'
	},
	mutations: {
		/**
		 * 设定路径
		 * @param state 数据
		 * @param payload 路径字符串
		 */
		setPath(state, payload) {
			state.path = payload;
		}
	},
	actions: {
		/**
		 * 设定路径
		 * @param context 上下文
		 * @param payload 路径字符串
		 */
		setPath(context, payload) {
			context.commit('setPath', payload);
		}
	}
};
import { sendRequest, REQUEST_METHOD } from '../../utils/request.js';
import { ElNotification } from 'element-plus';

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
		},
		/**
		 * 用户登录
		 * @param context 上下文
		 * @param payload 用于登录的用户请求体，要有username字段表示用户名或者邮箱，password字段表示密码
		 */
		async login(context, payload) {
			let response = await sendRequest('/api/user/login', REQUEST_METHOD.POST, payload);
			if (!response.success) {
				ElNotification({
					title: '错误！',
					message: response.message,
					type: 'error',
					duration: 200
				});
				return;
			}
			ElNotification({
				title: '成功！',
				message: '登录成功！1s后跳转...',
				type: 'success',
				duration: 750
			});
			// 获取用户信息
			await context.dispatch('checkLogin');
			setTimeout(() => {
				location.href = '/';
			}, 1000);
		},
		/**
		 * 退出登录
		 * @param context 上下文
		 */
		async logout(context) {
			let response = await sendRequest('/api/user/logout', REQUEST_METHOD.GET);
			ElNotification({
				title: '成功！',
				message: '退出登录成功！',
				type: 'success',
				duration: 750
			});
			// 清空数据与状态
			context.commit('setLogin', false);
			context.commit('setUserData', undefined);
			// 跳转
			location.href = '/login';
		}
	}
};
export default {
	namespaced: true,
	state: {
		// 是否显示菜单（手机端）
		menuShow: true,
		// 记录目录是否被解析完成，防止updated无限递归
		menuParsed: false,
		// 是否是夜晚样式
		isNight: false,
		// 是否是手机端
		isMobile: false,
		// 页面颜色主题
		pageColor: {
			pink: true,
			blue: false,
			green: false,
			orange: false,
			gray: false
		}
	},
	mutations: {
		/**
		 * 设定菜单是否显示
		 * @param state 数据
		 * @param payload 为布尔值，表示菜单是否显示
		 */
		setMenuShow(state, payload) {
			state.menuShow = payload;
		},
		/**
		 * 设定目录已解析完成
		 * @param state 数据
		 */
		setMenuParsedDone(state) {
			state.menuParsed = true;
		},
		/**
		 * 设定是否晚上
		 * @param state 数据
		 * @param payload 布尔值，表示是否是夜晚模式
		 */
		setIsNight(state, payload) {
			state.isNight = payload;
			// 存放设定到本地缓存
			window.localStorage.setItem('night', payload);
		},
		/**
		 * 设定是否是手机模式
		 * @param state 数据
		 * @param payload 布尔值，表示是否是手机模式
		 */
		setIsMobile(state, payload) {
			state.isMobile = payload;
		},
		/**
		 * 设定页面颜色
		 * @param state 数据
		 * @param payload 字符串，颜色值
		 */
		setPageColor(state, payload) {
			for (let key in state.pageColor) {
				state.pageColor[key] = false;
			}
			switch (payload) {
				case 'pink':
					state.pageColor.pink = true;
					break;
				case 'blue':
					state.pageColor.blue = true;
					break;
				case 'green':
					state.pageColor.green = true;
					break;
				case 'orange':
					state.pageColor.orange = true;
					break;
				case 'gray':
					state.pageColor.gray = true;
					break;
				default:
					state.pageColor.pink = true;
					break;
			}
			// 存放设定到本地缓存
			window.localStorage.setItem('color', payload);
		}
	},
	actions: {
		/**
		 * 设定菜单是否显示
		 * @param context 上下文
		 * @param payload 布尔值，表示菜单是否显示
		 */
		setMenuShow(context, payload) {
			if (!context.state.isMobile && payload === false) {
				return;
			}
			context.commit('setMenuShow', payload);
		},
		/**
		 * 设定目录已解析完成
		 * @param context 上下文对象
		 */
		setMenuParsedDone(context) {
			context.commit('setMenuParsedDone');
		},
		/**
		 * 设定是否是夜晚模式
		 * @param context 上下文
		 * @param payload 布尔值，表示是否是夜晚
		 */
		setIsNight(context, payload) {
			context.commit('setIsNight', payload);
		},
		/**
		 * 设定是否是手机模式
		 * @param context 上下文
		 * @param payload 布尔值，表示是否是手机模式
		 */
		setIsMobile(context, payload) {
			context.commit('setIsMobile', payload);
		},
		/**
		 * 设定页面主题颜色
		 * @param context 上下文
		 * @param payload 字符串，表示颜色值
		 */
		setPageColor(context, payload) {
			context.commit('setPageColor', payload);
		}
	}
};
import { sendRequest, REQUEST_METHOD } from '../utils/request';
import { parseImageURL, REQUEST_PREFIX } from '../param/request-prefix';
import { defineStore } from 'pinia';

export const useMetaDataStore = defineStore('meta-data', {
	state() {
		return {
			/**
			 * 组织名
			 */
			organizationName: '获取中...',
			/**
			 * 是否允许公开注册
			 */
			allowPublic: false,
			/**
			 * 后端运行系统用户（用于拼接克隆地址）
			 */
			systemUser: undefined,
			/**
			 * 后端服务器SSH端口（用于拼接克隆地址）
			 */
			sshPort: 22,
			/**
			 * 登录页背景图片（请求地址）
			 */
			loginBackground: undefined,
			/**
			 * 主页面背景图片（请求地址）
			 */
			mainBackground: undefined
		};
	},
	actions: {
		/**
		 * 从后端获取组织名
		 */
		async requestName() {
			const response = await sendRequest(REQUEST_PREFIX.SYSTEM_SETTING + 'get-organization', REQUEST_METHOD.GET);
			if (response.success) {
				this.organizationName = response.data;
			}
		},
		/**
		 * 从后端获取是否允许公开注册
		 */
		async requestAllowPublic() {
			const response = await sendRequest(REQUEST_PREFIX.SYSTEM_SETTING + 'get-allow-public', REQUEST_METHOD.GET);
			if (response.success) {
				this.allowPublic = response.data;
			}
		},
		/**
		 * 获取后端系统用户
		 */
		async getSystemUser() {
			const response = await sendRequest(REQUEST_PREFIX.SYSTEM_INFO + 'system-user', REQUEST_METHOD.GET);
			if (response.success) {
				this.systemUser = response.data;
			}
		},
		/**
		 * 获取SSH端口
		 */
		async getSSHPort() {
			const response = await sendRequest(REQUEST_PREFIX.SYSTEM_INFO + 'ssh-port', REQUEST_METHOD.GET);
			if (response.success) {
				this.sshPort = response.data;
			}
		},
		/**
		 * 获取登录页背景图片
		 */
		async getLoginImage() {
			// 首先获取自定义图片
			const response = await sendRequest(REQUEST_PREFIX.SYSTEM_SETTING + 'get-login-image', REQUEST_METHOD.GET);
			if (response.data != null) {
				this.loginBackground = parseImageURL(response.data);
				return;
			}
			// 若自定义图片为空，则使用默认图片
			// 根据时间动态导入图片
			let imageImport;
			const hours = new Date().getHours();
			if (hours >= 7 && hours < 18) {
				imageImport = await import('../assets/background/login/daytime.jpg');
			} else if (hours >= 18 && hours < 21) {
				imageImport = await import('../assets/background/login/evening.jpg');
			} else {
				imageImport = await import('../assets/background/login/night.jpg');
			}
			this.loginBackground = imageImport.default;
		},
		/**
		 * 获取主页背景图片
		 */
		async getMainImage() {
			// 首先获取自定义图片
			const response = await sendRequest(REQUEST_PREFIX.SYSTEM_SETTING + 'get-main-image', REQUEST_METHOD.GET);
			if (response.data != null) {
				this.mainBackground = parseImageURL(response.data);
				return;
			}
			// 若自定义图片为空，则使用默认图片
			// 根据月份动态导入图片
			let imageImport;
			const month = new Date().getMonth();
			if (month >= 2 && month <= 4) {
				imageImport = await import('../assets/background/main/spring.jpg');
			} else if (month >= 5 && month <= 7) {
				imageImport = await import('../assets/background/main/summer.jpg');
			} else if (month >= 8 && month <= 10) {
				imageImport = await import('../assets/background/main/autumn.jpg');
			} else {
				imageImport = await import('../assets/background/main/winter.jpg');
			}
			this.mainBackground = imageImport.default;
		},
		/**
		 * 从后端获取全部元数据信息
		 */
		async getAllMeta() {
			// 发起请求获取路径
			await this.requestName();
			await this.requestAllowPublic();
			await this.getSystemUser();
			await this.getSSHPort();
			await this.getLoginImage();
			await this.getMainImage();
		},
		/**
		 * 设置当前页的标签标题
		 * @param title 传入标题
		 */
		setTitle(title) {
			document.title = this.organizationName + ' | ' + title;
		}
	}
});
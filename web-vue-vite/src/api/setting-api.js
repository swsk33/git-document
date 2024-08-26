// 用户设置接口请求

import { REQUEST_METHOD, sendRequest } from '../utils/request.js';

/**
 * @typedef Setting 设置对象
 * @property {Number} userId 关联的用户id
 * @property {Boolean} receiveUpdateEmail 是否接受更新邮件
 * @property {Boolean} receiveNewEmail 是否接受新文集邮件
 */

const apiPrefix = '/api/setting';

/**
 * 更新一个用户设置
 * @param {Setting} setting 更新的设置对象
 * @returns {Promise<Result<void>>} 响应体
 */
export async function settingUpdate(setting) {
	return sendRequest(`${apiPrefix}/update`, REQUEST_METHOD.PATCH, setting);
}
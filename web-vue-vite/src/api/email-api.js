// 邮件服务接口请求

import { REQUEST_METHOD, sendRequest } from '../utils/request.js';

const apiPrefix = '/api/email';

/**
 * 发送重置密码邮件验证码
 * @param {String} email 邮箱
 * @returns {Promise<Result<void>>} 响应体
 */
export async function emailPasswordResetCode(email) {
	return sendRequest(`${apiPrefix}/password-reset/${email}`, REQUEST_METHOD.GET);
}
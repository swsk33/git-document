// 获取系统运行参数接口请求

import { REQUEST_METHOD, sendRequest } from '../utils/request.js';

const apiPrefix = '/api/system-info';

/**
 * 获取后端系统运行的用户名
 * @returns {Promise<Result<String>>} 包含用户名的结果
 */
export async function systemInfoGetUser() {
	return sendRequest(`${apiPrefix}/system-user`, REQUEST_METHOD.GET);
}

/**
 * 获取后端SSH广播端口号
 * @returns {Promise<Result<Number>>} 包含SSH端口号的结果
 */
export async function systemInfoGetSSHPort() {
	return sendRequest(`${apiPrefix}/ssh-port`, REQUEST_METHOD.GET);
}
// 收藏请求接口

import { REQUEST_METHOD, sendRequest } from '../utils/request.js';

/**
 * @typedef Star 收藏（星星）对象
 * @property {String} id 主键id
 * @property {Number} userId 关联的用户id
 * @property {String} anthologyId 收藏的文集id
 */

const apiPrefix = '/api/star';

/**
 * 新增一个收藏
 * @param {String} anthologyId 收藏的文集id
 * @returns {Promise<Result<Star>>} 响应体，包含新增的收藏信息
 */
export async function starAdd(anthologyId) {
	return sendRequest(`${apiPrefix}/add`, REQUEST_METHOD.POST, {
		anthologyId: anthologyId
	});
}

/**
 * 取消收藏
 * @param {String} id 收藏id
 * @returns {Promise<Result<void>>} 响应体
 */
export async function starDelete(id) {
	return sendRequest(`${apiPrefix}/delete/${id}`, REQUEST_METHOD.DELETE);
}

/**
 * 获取当前登录用户的收藏列表
 * @returns {Promise<Result<Array<Star>>>} 收藏列表响应体
 */
export async function starGetByLoginUser() {
	return sendRequest(`${apiPrefix}/get-by-login-user`, REQUEST_METHOD.GET);
}
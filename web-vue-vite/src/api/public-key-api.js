// 公钥接口请求

import { REQUEST_METHOD, sendRequest } from '../utils/request.js';

/**
 * @typedef PublicKey 公钥对象
 * @property {Number} id 主键id
 * @property {String} content 公钥内容
 * @property {Number} userId 所属用户id
 */

const apiPrefix = '/api/public-key';

/**
 * 新增一个公钥对象
 * @param {PublicKey} publicKey 公钥对象
 * @returns {Promise<Result<void>>} 响应体
 */
export async function publicKeyAdd(publicKey) {
	return sendRequest(`${apiPrefix}/add`, REQUEST_METHOD.POST, publicKey);
}

/**
 * 删除一个公钥对象
 * @param {Number} id 公钥id
 * @returns {Promise<Result<void>>} 响应体
 */
export async function publicKeyDelete(id) {
	return sendRequest(`${apiPrefix}/delete/${id}`, REQUEST_METHOD.DELETE);
}
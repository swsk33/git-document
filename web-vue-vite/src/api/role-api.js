// 角色请求接口

import { REQUEST_METHOD, sendRequest } from '../utils/request.js';

/**
 * @typedef Permission 权限对象
 * @property {Number} id 主键id
 * @property {String} name 名称
 * @property {String} showName 显示名称
 *
 * @typedef Role 角色对象
 * @property {Number} id 主键id
 * @property {String} name 名称
 * @property {String} showName 显示名称
 * @property {Array<Permission>} permissions 权限列表
 */

const apiPrefix = '/api/role';

/**
 * 获取全部角色
 * @returns {Promise<Result<Array<Role>>>} 全部角色列表
 */
export async function roleGetAll() {
	return sendRequest(`${apiPrefix}/get-all`, REQUEST_METHOD.GET);
}
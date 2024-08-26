// 用户相关接口请求

/**
 * @typedef User 用户对象
 * @property {Number} id 主键id
 * @property {String} username 用户名
 * @property {String} password 密码
 * @property {String} nickname 昵称
 * @property {String} avatar 头像id
 * @property {String} email 邮箱
 * @property {Number} roleId 用户角色id
 * @property {Role} role 用户角色
 * @property {LoginRecord} loginRecord 上次登录记录
 * @property {Setting} setting 用户偏好设定
 * @property {Array<Star>} stars 用户的收藏列表
 * @property {Array<PublicKey>} keys 用户的公钥
 *
 * @typedef LoginRecord 用户登录记录
 * @property {Number} userId 关联用户id
 * @property {String} ip 上次登录ip
 * @property {String} location 上次登录位置
 * @property {Date} gmtCreated 记录创建时间
 * @property {Date} gmtModified 上次登录时间
 */

import { REQUEST_METHOD, sendRequest } from '../utils/request.js';

const apiPrefix = '/api/user';

/**
 * 注册用户
 * @param {User} user 传入用户对象
 * @returns {Promise<Result<void>>} 响应体
 */
export async function userRegister(user) {
	return sendRequest(`${apiPrefix}/register`, REQUEST_METHOD.POST, user);
}

/**
 * 用户登录
 * @param {User} user 传入用户对象
 * @returns {Promise<Result<void>>} 响应体
 */
export async function userLogin(user) {
	return sendRequest(`${apiPrefix}/login`, REQUEST_METHOD.POST, user);
}

/**
 * 用户重置密码
 * @param {User} user 重置密码后的用户对象
 * @param {Number | String} code 验证码
 * @returns {Promise<Result<void>>} 响应体
 */
export async function userResetPassword(user, code) {
	return sendRequest(`${apiPrefix}/reset-password/${code}`, REQUEST_METHOD.POST, user);
}

/**
 * 注销用户账户
 * @param {Number} id 用户id
 * @returns {Promise<Result<void>>} 响应体
 */
export async function userDelete(id) {
	return sendRequest(`${apiPrefix}/delete/${id}`, REQUEST_METHOD.DELETE);
}

/**
 * 更新用户信息
 * @param {User} user 更新后的用户信息
 * @returns {Promise<Result<void>>} 响应体
 */
export async function userUpdate(user) {
	return sendRequest(`${apiPrefix}/update`, REQUEST_METHOD.PATCH, user);
}

/**
 * 判断用户是否登录
 * @returns {Promise<Result<User>>} 用户登录则返回包含用户完整信息的响应
 */
export async function userIsLogin() {
	return sendRequest(`${apiPrefix}/is-login`, REQUEST_METHOD.GET);
}

/**
 * 获取全部用户信息
 * @returns {Promise<Result<Array<User>>>} 用户信息列表
 */
export async function userGetAll() {
	return sendRequest(`${apiPrefix}/get-all`, REQUEST_METHOD.GET);
}

/**
 * 用户退出登录
 * @returns {Promise<Result<void>>} 响应体
 */
export async function userLogout() {
	return sendRequest(`${apiPrefix}/logout`, REQUEST_METHOD.GET);
}
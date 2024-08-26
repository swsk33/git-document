// 后端系统设置接口请求

import { REQUEST_METHOD, sendRequest, uploadFile } from '../utils/request.js';

const apiPrefix = '/api/system-setting';

/**
 * 设定系统登录背景图
 * @param imageFile 图片文件（从input元素获取）
 * @returns {Promise<Result<void>>} 响应体
 */
export async function systemSetLoginImage(imageFile) {
	const form = new FormData();
	form.append('image', imageFile);
	return uploadFile(`${apiPrefix}/set-login-image`, REQUEST_METHOD.POST, form);
}

/**
 * 设定系统主页背景图
 * @param imageFile 图片文件（从input元素获取）
 * @returns {Promise<Result<void>>} 响应体
 */
export async function systemSetMainImage(imageFile) {
	const form = new FormData();
	form.append('image', imageFile);
	return uploadFile(`${apiPrefix}/set-main-image`, REQUEST_METHOD.POST, form);
}

/**
 * 设定组织名
 * @param {String} name 设定的组织名
 * @returns {Promise<Result<void>>} 响应体
 */
export async function systemSetOrganizationName(name) {
	return sendRequest(`${apiPrefix}/set-organization/${name}`, REQUEST_METHOD.PUT);
}

/**
 * 设定是否允许公开访问
 * @param {Boolean} allow 是否允许公开
 * @returns {Promise<Result<void>>} 响应体
 */
export async function systemSetAllowPublic(allow) {
	return sendRequest(`${apiPrefix}/set-allow-public/${allow}`, REQUEST_METHOD.PUT);
}

/**
 * 重置登录背景图
 * @returns {Promise<Result<void>>} 响应体
 */
export async function systemResetLoginImage() {
	return sendRequest(`${apiPrefix}/reset-login-image`, REQUEST_METHOD.GET);
}

/**
 * 重置主页背景图
 * @returns {Promise<Result<void>>} 响应体
 */
export async function systemResetMainImage() {
	return sendRequest(`${apiPrefix}/reset-main-image`, REQUEST_METHOD.GET);
}

/**
 * 获取组织名
 * @returns {Promise<Result<String>>} 组织名
 */
export async function systemGetOrganizationName() {
	return sendRequest(`${apiPrefix}/get-organization`, REQUEST_METHOD.GET);
}

/**
 * 获取是否允许公开
 * @returns {Promise<Result<Boolean>>} 是否允许公开
 */
export async function systemGetAllowPublic() {
	return sendRequest(`${apiPrefix}/get-allow-public`, REQUEST_METHOD.GET);
}

/**
 * 获取登录背景图
 * @returns {Promise<Result<String>>} 背景图片文件名，若为空则说明使用默认背景
 */
export async function systemGetLoginImage() {
	return sendRequest(`${apiPrefix}/get-login-image`, REQUEST_METHOD.GET);
}

/**
 * 获取主页背景图
 * @returns {Promise<Result<String>>} 背景图片文件名，若为空则说明使用默认背景
 */
export async function systemGetMainImage() {
	return sendRequest(`${apiPrefix}/get-main-image`, REQUEST_METHOD.GET);
}
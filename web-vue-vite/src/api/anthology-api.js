// 关于文集仓库的接口请求

import { REQUEST_METHOD, sendRequest } from '../utils/request.js';

/**
 * @typedef Anthology 文集仓库对象
 * @property {String} id 主键id
 * @property {String} name 名称
 * @property {String} showName 显示名
 * @property {String} cover 封面图片id
 * @property {Number} updateTime 更新时间的时间戳
 * @property {String} repoPath 文集仓库路径
 * @property {String} latestCommit 最新的一次提交id
 * @property {String} status 仓库状态
 * @property {Array<Star>} stars 该仓库的收藏列表
 */

const apiPrefix = '/api/anthology';

/**
 * 新增一个文集仓库
 * @param {Anthology} anthology 文集仓库对象
 * @returns {Promise<Result<void>>} 响应体
 */
export async function anthologyAdd(anthology) {
	return sendRequest(`${apiPrefix}/add`, REQUEST_METHOD.POST, anthology);
}

/**
 * 删除文集仓库
 * @param {String} id 文集仓库id
 * @returns {Promise<Result<void>>} 响应体
 */
export async function anthologyDelete(id) {
	return sendRequest(`${apiPrefix}/delete/${id}`, REQUEST_METHOD.DELETE);
}

/**
 * 更新文集仓库
 * @param {Anthology} anthology 更新的文集仓库对象
 * @returns {Promise<Result<void>>} 响应体
 */
export async function anthologyUpdate(anthology) {
	return sendRequest(`${apiPrefix}/update`, REQUEST_METHOD.PATCH, anthology);
}

/**
 * 根据id获取文集仓库信息
 * @param {String} id 文集仓库id
 * @returns {Promise<Result<Anthology>>} 包含文集仓库信息的响应体
 */
export async function anthologyGetById(id) {
	return sendRequest(`${apiPrefix}/get/${id}`, REQUEST_METHOD.GET);
}

/**
 * 获取全部文集仓库
 * @returns {Promise<Result<Array<Anthology>>>} 包含全部文集仓库的响应体
 */
export async function anthologyGetAll() {
	return sendRequest(`${apiPrefix}/get-all`, REQUEST_METHOD.GET);
}

/**
 * 获取一个文集仓库的全部贡献记录
 * @param {String} id 文集仓库id
 * @returns {Promise<Result<Array>>} 贡献记录
 */
export async function anthologyGetCommit(id) {
	return sendRequest(`${apiPrefix}/get-all-commits/${id}`, REQUEST_METHOD.GET);
}

/**
 * 获取在本地但是没录入数据库的文集仓库
 * @returns {Promise<Result<Array<Anthology>>>} 在本地但是没录入数据库的文集仓库
 */
export async function anthologyGetNotInDB() {
	return sendRequest(`${apiPrefix}/get-not-in-database`, REQUEST_METHOD.GET);
}

/**
 * 批量增加文集仓库
 * @param {Array<Anthology>} anthologyList 文集仓库列表
 * @returns {Promise<Result<void>>} 响应体
 */
export async function anthologyRestore(anthologyList) {
	return sendRequest(`${apiPrefix}/restore-not-in-database`, REQUEST_METHOD.POST, anthologyList);
}
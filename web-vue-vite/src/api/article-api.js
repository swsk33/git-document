// 请求文章接口的操作

import { REQUEST_METHOD, sendRequest } from '../utils/request.js';

/**
 * @typedef Article 文章数据模型
 * @property {String} id 主键id
 * @property {String} filePath 文章路径
 * @property {String} content 文章内容
 * @property {String} anthologyId 所属文集id
 *
 * @typedef ArticleFile 文章文件模型
 * @property {String} id 文章id
 * @property {String} name 文章名
 *
 * @typedef ArticleFileTree 文档文件目录树，该对象可以表示一个文件夹
 * @property {String} name 目录名
 * @property {Array<ArticleFileTree>} directories 该目录下的目录列表
 * @property {Array<ArticleFile>} articles 该目录下文件列表
 *
 * @typedef ArticleTreeNode 组件式树结构的节点
 * @property {String} id 节点id（即为文章id，如果是目录则id为null）
 * @property {String} label 当前节点显示名
 * @property {Array<ArticleTreeNode>} children 子节点
 */

const apiPrefix = '/api/article';

/**
 * 根据文集id获取一个文集的全部文章列表
 * @param {String} anthologyId 文集id
 * @returns {Promise<Result<ArticleFileTree>>} 文章目录树
 */
export async function articleGetList(anthologyId) {
	return sendRequest(`${apiPrefix}/get-article-list/${anthologyId}`, REQUEST_METHOD.GET);
}

/**
 * 获取一个文章的信息
 * @param {String} id 文章id
 * @returns {Promise<Result<Article>>} 文章对象响应体
 */
export async function articleGet(id) {
	return sendRequest(`${apiPrefix}/get/${id}`, REQUEST_METHOD.GET);
}
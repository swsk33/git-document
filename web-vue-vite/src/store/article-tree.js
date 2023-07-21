// 在这个Store中，定义文档目录树结构以及解析方式，将后端的文件系统形式目录树转化为Element-Plus中树形组件形式

/**
 * @typedef Article 文章
 * @property {String} id 文章id
 * @property {String} name 文章名
 *
 * @typedef ArticleFileTree 文档文件目录树，该对象可以表示一个文件夹
 * @property {String} name 目录名
 * @property {Array<ArticleFileTree>} directories 该目录下的目录列表
 * @property {Array<Article>} articles 该目录下文件列表
 *
 * @typedef ArticleTreeNode 组件式树结构的节点
 * @property {String} id 节点id（即为文章id，如果是目录则id为null）
 * @property {String} label 当前节点显示名
 * @property {Array<ArticleTreeNode>} children 子节点
 */

/**
 * 组件树节点构造函数
 * @param {String} id 节点id（即为文章id，如果不是叶子节点则id为null）
 * @param {String} label 节点显示名
 * @param {Array<ArticleTreeNode>} children 节点下子节点数组，如果是叶子节点则为undefined
 * @constructor 组件树构造函数
 */
function ArticleTreeNode(id, label, children) {
	this.id = id;
	this.label = label;
	if (children !== undefined) {
		this.children = children;
	}
}

import { defineStore } from 'pinia';

export const useArticleTreeStore = defineStore('articleTree', {
	state() {
		return {
			/**
			 * 当前正在浏览的文章所在文集的文章目录树，从缓存读取
			 */
			currentArticleTree: undefined
		};
	},
	actions: {
		/**
		 * 解析文章文件目录树对象为组件目录树形式
		 * @param {ArticleFileTree} articleFileTree 文件目录树
		 * @param {String} name 该节点名称
		 * @return {ArticleTreeNode} 组件树
		 */
		parseArticleTree(articleFileTree, name = '根目录') {
			// 该层次根节点
			const root = new ArticleTreeNode(null, name, []);
			// 递归地添加文件目录树下的目录
			for (let item of articleFileTree.directories) {
				root.children.push(this.parseArticleTree(item, item.name));
			}
			// 然后将文件目录树下的文件加入到其中
			for (let item of articleFileTree.articles) {
				root.children.push(new ArticleTreeNode(item.id, item.name, undefined));
			}
			return root;
		},
		/**
		 * 把文章组件树存到缓存中去
		 * @param {String} id 文集id
		 * @param {ArticleTreeNode} articleTreeNode 组件树对象
		 */
		saveArticleTree(id, articleTreeNode) {
			localStorage.setItem('anthology-' + id, JSON.stringify(articleTreeNode));
		},
		/**
		 * 从缓存读取文章目录树
		 * @param {String} id 文集id
		 */
		getArticleTree(id) {
			this.currentArticleTree = JSON.parse(localStorage.getItem('anthology-' + id));
		}
	}
});
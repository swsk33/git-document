// 关于文集仓库信息的储存器

import { defineStore } from 'pinia';
import { anthologyDelete, anthologyGetAll, anthologyGetById, anthologyUpdate } from '../api/anthology-api.js';

export const useAnthologyStore = defineStore('anthology', {
	state() {
		return {
			/**
			 * 存放全部文集仓库对象的哈希表<br>
			 * - 键：文集仓库id
			 * - 值：文集仓库对象
			 */
			anthologyMap: new Map(),
			/**
			 * 每个文集仓库对应的收藏数量哈希表<br>
			 * - 键：文集仓库id
			 * - 值：文集仓库的收藏数量
			 */
			starCountMap: new Map()
		};
	},
	actions: {
		/**
		 * 获取全部文集仓库，并更新到anthologyMap和starCountMap
		 */
		async getAll() {
			this.anthologyMap.clear();
			this.starCountMap.clear();
			const response = await anthologyGetAll();
			if (response.success) {
				for (let item of response.data) {
					this.anthologyMap.set(item.id, item);
					this.starCountMap.set(item.id, item.stars.length);
				}
			}
			return response;
		},
		/**
		 * 更新某个文集仓库的信息
		 * @param anthology 修改信息后更新的文集对象
		 */
		async updateOne(anthology) {
			const updateResponse = await anthologyUpdate(anthology);
			if (!updateResponse.success) {
				return updateResponse;
			}
			// 刷新该文集在本地的信息
			const getResponse = await anthologyGetById(anthology.id);
			if (getResponse.success) {
				this.anthologyMap.set(anthology.id, getResponse.data);
				this.starCountMap.set(anthology.id, getResponse.data.stars.length);
			}
			return updateResponse;
		},
		/**
		 * 删除某个文集，并将其从视图列表移除
		 * @param id 待删除的文集id
		 */
		async removeOne(id) {
			const response = await anthologyDelete(id);
			if (response.success) {
				this.anthologyMap.delete(id);
				this.starCountMap.delete(id);
			}
			return response;
		},
		/**
		 * 将某个文集显示收藏数+1
		 * @param id 文集id
		 */
		starCountAdd(id) {
			const count = this.starCountMap.get(id);
			this.starCountMap.set(id, count + 1);
		},
		/**
		 * 将某个文集显示收藏数-1
		 * @param id 文集id
		 */
		starCountSubtract(id) {
			const count = this.starCountMap.get(id);
			this.starCountMap.set(id, count - 1);
		}
	}
});
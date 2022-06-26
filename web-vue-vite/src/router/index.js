// 用于主面板页面的router
import {
	createRouter, createWebHistory
} from 'vue-router';

import anthologyList from '../views/AnthologyList.vue';

const routes = [
	{
		path: '/',
		component: anthologyList
	},
	{
		path: '/interior-anthology',
		component: anthologyList
	},
	{
		path: '/anthology-menu/:id',
		component: () => import('../views/AnthologyMenu.vue')
	}
];

const router = createRouter({
	history: createWebHistory(), routes
});

export default router;
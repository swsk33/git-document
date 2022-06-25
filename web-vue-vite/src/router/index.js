import {
	createRouter, createWebHistory
} from 'vue-router';

import anthologyList from '../views/AnthologyList.vue';

const routes = [
	{
		path: '/article/:id',
		component: () => import('../pages/article/views/MainBody.vue')
	},
	{
		path: '/login',
		component: () => import('../pages/login/views/LoginPanel.vue')
	},
	{
		path: '/login/register',
		component: () => import('../pages/login/views/RegisterPanel.vue')
	},
	{
		path: '/',
		component: anthologyList
	},
	{
		path: '/interior-anthology',
		component: anthologyList
	}
];

const router = createRouter({
	history: createWebHistory(), routes
});

export default router;
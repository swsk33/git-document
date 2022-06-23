import {
	createRouter, createWebHistory
} from 'vue-router';

const routes = [
	{
		path: '/article/:id',
		component: () => import('../pages/article/components/MainBody.vue')
	},
	{
		path: '/login',
		component: () => import('../pages/login/components/LoginPanel.vue')
	},
	{
		path: '/login/register',
		component: () => import('../pages/login/components/RegisterPanel.vue')
	}
];

const router = createRouter({
	history: createWebHistory(), routes
});

export default router;
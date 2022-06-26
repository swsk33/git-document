// 用于登录页的vue router
import {
	createRouter, createWebHistory
} from 'vue-router';

const routes = [
	{
		path: '/login',
		component: () => import('../pages/login/views/LoginPanel.vue')
	},
	{
		path: '/login/register',
		component: () => import('../pages/login/views/RegisterPanel.vue')
	}
];

const router = createRouter({
	history: createWebHistory(), routes
});

export default router;
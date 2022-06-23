import {
	createRouter, createWebHistory
} from 'vue-router';

const routes = [
	{
		path: '/article/:id',
		component: () => import('../pages/article/components/MainBody.vue')
	}
];

const router = createRouter({
	history: createWebHistory(), routes
});

export default router;
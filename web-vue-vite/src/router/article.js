// 用于文章内容页面的路由
import {
	createRouter, createWebHistory
} from 'vue-router';

const routes = [
	{
		path: '/article/:id',
		component: () => import('../pages/article/views/MainBody.vue')
	}
];

const router = createRouter({
	history: createWebHistory(), routes
});

export default router;
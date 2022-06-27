import {
	createRouter, createWebHistory
} from 'vue-router';

// 登录页路由
const loginPageRouter = {
	path: '/login',
	component: () => import('../pageviews/login/LoginPage.vue'),
	children: [
		{
			path: '',
			component: () => import('../pageviews/login/views/LoginPanel.vue')
		},
		{
			path: 'register',
			component: () => import('../pageviews/login/views/RegisterPanel.vue')
		}
	]
};

// 主面板路由
const mainPanelRouter = {
	path: '/',
	component: () => import('../pageviews/mainpanel/MainPanel.vue'),
	redirect: '/interior-anthology',
	children: [
		{
			path: 'interior-anthology',
			component: () => import('../pageviews/mainpanel/views/AnthologyList.vue')
		},
		{
			path: 'anthology-menu/:id',
			component: () => import('../pageviews/mainpanel/views/AnthologyMenu.vue')
		}
	]
};

// 文章页路由
const articleRouter = {
	path: '/article/:id',
	component: () => import('../pageviews/article/ArticlePage.vue')
};

const routes = [loginPageRouter, mainPanelRouter, articleRouter];

const router = createRouter({
	history: createWebHistory(), routes
});

export default router;
import {
	createRouter, createWebHistory
} from 'vue-router';

// 登录页路由
const loginPageRouter = {
	path: '/login',
	component: () => import('../page-views/login/LoginPage.vue'),
	children: [
		{
			path: '',
			component: () => import('../page-views/login/views/LoginPanel.vue')
		},
		{
			path: 'register',
			component: () => import('../page-views/login/views/RegisterPanel.vue')
		},
		{
			path: 'forget-password',
			component: () => import('../page-views/login/views/ForgetPassword.vue')
		}
	]
};

// 主面板路由
const mainPanelRouter = {
	path: '/',
	component: () => import('../page-views/main-panel/MainPanel.vue'),
	redirect: '/interior-anthology',
	children: [
		{
			path: 'interior-anthology',
			component: () => import('../page-views/main-panel/views/AnthologyList.vue')
		},
		{
			path: 'article-menu/:id',
			component: () => import('../page-views/main-panel/views/ArticleMenu.vue')
		},
		{
			path: 'committer/:id',
			component: () => import('../page-views/main-panel/views/CommitterList.vue')
		},
		{
			path: 'user-manage',
			component: () => import('../page-views/main-panel/views/UserManage.vue')
		},
		{
			path: 'system-setting',
			component: () => import('../page-views/main-panel/views/SystemSetting.vue')
		},
		{
			path: 'my',
			component: () => import('../page-views/main-panel/views/MyInfoEdit.vue')
		},
		// 404页面
		{
			path: '/:pathMatch(.*)*',
			component: () => import('../page-views/main-panel/views/NotFoundView.vue')
		}
	]
};

// 文章页路由
const articleRouter = {
	path: '/article/:id',
	component: () => import('../page-views/article/ArticlePage.vue')
};

const routes = [loginPageRouter, mainPanelRouter, articleRouter];

const router = createRouter({
	history: createWebHistory(),
	routes
});

export default router;
import { createApp } from 'vue';
import App from '../src/pages/article/ArticlePage.vue';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import store from '../src/store';
import router from '../src/router/article.js';

// 创建实例
const app = createApp(App);
// 使用组件
app.use(ElementPlus);
app.use(store);
app.use(router);
// 挂载
app.mount('#app');
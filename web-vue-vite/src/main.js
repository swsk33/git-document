import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
// 引入外部库的css
import 'element-plus/dist/index.css';
import 'katex/dist/katex.css';

// 创建实例
const app = createApp(App);
// 使用组件
app.use(router);
app.use(createPinia());
// 挂载
app.mount('#app');
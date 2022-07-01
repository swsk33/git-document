import { createApp } from 'vue';
import App from './App.vue';
import store from './store';
import router from './router';
import 'element-plus/dist/index.css';

// 创建实例
const app = createApp(App);
// 使用组件
app.use(store);
app.use(router);
// 挂载
app.mount('#app');
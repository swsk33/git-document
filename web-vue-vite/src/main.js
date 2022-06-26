import { createApp } from 'vue';
import PanelMain from './MainPanel.vue';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import store from './store';
import router from './router/index.js';

// 创建实例
const app = createApp(PanelMain);
// 使用组件
app.use(ElementPlus);
app.use(store);
app.use(router);
// 挂载
app.mount('#app');
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
	plugins: [vue()],
	server: {
		// 跨域配置
		proxy: {
			// 请求代理
			'/api': {
				target: 'http://127.0.0.1:8801/',
				changeOrigin: true
			},
			// 静态资源代理
			'/static': {
				target: 'http://127.0.0.1:8801/',
				changeOrigin: true
			}
		}
	}
});
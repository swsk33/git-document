import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import { resolve } from 'path';

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
	},
	// 多页应用构建配置
	build: {
		rollupOptions: {
			input: {
				panel: resolve(__dirname, 'index.html'),
				article: resolve(__dirname, 'article/index.html'),
				login: resolve(__dirname, 'login/index.html')
			}
		}
	}
});
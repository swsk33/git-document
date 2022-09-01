import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
// element-plus自动按需导入
import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers';

export default defineConfig({
	plugins: [
		vue(),
		// 配置element-plus自动按需导入
		AutoImport({
			resolvers: [ElementPlusResolver()]
		}),
		Components({
			resolvers: [ElementPlusResolver()]
		})],
	server: {
		// 跨域配置
		proxy: {
			// 请求代理
			'/api': {
				target: 'http://127.0.0.1:8800/',
				changeOrigin: true
			},
			// 静态资源代理
			'/static': {
				target: 'http://127.0.0.1:8800/',
				changeOrigin: true
			}
		}
	}
});
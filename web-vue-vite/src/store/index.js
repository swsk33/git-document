import {
	createStore
} from 'vuex';

// 批量导入全部的模块
const modules = import.meta.globEager('./modules/*.js');

// 对批量导入存放模块的对象进行处理
const keys = Object.keys(modules);
for (let path of keys) {
	// 裁剪字符串方式得到路径中的文件名（无扩展名）
	let name = path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.js'));
	// 对原对象执行添加新的属性并删除旧属性达到处理效果
	modules[name] = modules[path].default;
	delete modules[path];
}


// 定义总的store
const store = {
	modules: {}
};

// 把批量导入的模块塞进store
for (let key in modules) {
	store.modules[key] = modules[key];
}

// 最后导出vuex store
export default createStore(store);
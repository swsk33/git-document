// 文件路径处理

/**
 * 相对路径的拼接
 * @param {string} relative 相对于该文章的文件路径
 * @param {string} filePathInRepo 该文章在Git文集仓库中的路径
 * @return {string} 拼接后相对路径位于文集仓库中的路径
 */
function joinPath(relative, filePathInRepo) {
	const relativeArray = relative.split('/');
	const filePathArray = filePathInRepo.split('/');
	for (let i = 0; i < relativeArray.length; i++) {
		if (relativeArray[i] === '..') {
			relativeArray.shift();
			filePathArray.pop();
			i--;
			continue;
		}
		if (relativeArray[i] === '.') {
			relativeArray.shift();
			i--;
			continue;
		}
		break;
	}
	return filePathArray.join('/') + '/' + relativeArray.join('/');
}

export { joinPath };
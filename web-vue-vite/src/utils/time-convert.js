/**
 * 时间戳转时间字符串
 * @param timestamp 时间戳
 * @return {string} 时间字符串
 */
function timestampToDateString(timestamp) {
	if (timestamp === 0) {
		return '暂无提交';
	}
	const time = new Date(timestamp * 1000);
	return time.getFullYear() + '.' + (time.getMonth() + 1) + '.' + time.getDate() + ' ' + time.getHours() + ':' + time.getMinutes() + ':' + time.getSeconds();
}

export { timestampToDateString };
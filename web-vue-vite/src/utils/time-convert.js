/**
 * 时间戳转时间字符串
 * @param timestamp 时间戳
 * @return {string} 时间字符串
 */
function timestampToDateString(timestamp) {
	if (timestamp == null) {
		return '暂无提交';
	}
	const time = new Date(timestamp * 1000);
	const hour = time.getHours() < 10 ? '0' + time.getHours() : time.getHours();
	const minute = time.getMinutes() < 10 ? '0' + time.getMinutes() : time.getMinutes();
	const second = time.getSeconds() < 10 ? '0' + time.getSeconds() : time.getSeconds();
	return time.getFullYear() + '.' + (time.getMonth() + 1) + '.' + time.getDate() + ' ' + hour + ':' + minute + ':' + second;
}

export { timestampToDateString };
/**
 * 时间戳转时间字符串
 * @param timestamp 时间戳
 * @return {string} 时间字符串
 */
function timestampToDateString(timestamp) {
	if (timestamp == null) {
		return '暂无提交';
	}
	return dateToString(new Date(timestamp * 1000));
}

/**
 * 将时间date对象转换成可读字符串
 * @param {Date | string} time 时间对象或者时间字符串
 * @return {string} 时间字符串
 */
function dateToString(time) {
	if (typeof time === 'string') {
		time = new Date(time);
	}
	const hour = time.getHours() < 10 ? '0' + time.getHours() : time.getHours();
	const minute = time.getMinutes() < 10 ? '0' + time.getMinutes() : time.getMinutes();
	const second = time.getSeconds() < 10 ? '0' + time.getSeconds() : time.getSeconds();
	return `${time.getFullYear()}.${(time.getMonth() + 1)}.${time.getDate()} ${hour}:${minute}:${second}`;
}

export { timestampToDateString, dateToString };
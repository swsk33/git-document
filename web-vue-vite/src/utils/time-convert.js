/**
 * 时间戳转时间字符串
 * @param {Number} timestamp 时间戳
 * @param {string} defaultValue 当传入时间为空时，默认的返回内容
 * @return {string} 时间字符串
 */
function timestampToDateString(timestamp, defaultValue = '') {
	if (timestamp == null) {
		return defaultValue;
	}
	return dateToString(new Date(timestamp * 1000));
}

/**
 * 将时间date对象转换成可读字符串
 * @param {Date | string} time 时间对象或者时间字符串
 * @param {string} defaultValue 当传入时间为空时，默认的返回内容
 * @return {string} 时间字符串
 */
function dateToString(time, defaultValue = '') {
	if (time == null || time === '') {
		return defaultValue;
	}
	if (typeof time === 'string') {
		time = new Date(time);
	}
	const hour = time.getHours() < 10 ? '0' + time.getHours() : time.getHours();
	const minute = time.getMinutes() < 10 ? '0' + time.getMinutes() : time.getMinutes();
	const second = time.getSeconds() < 10 ? '0' + time.getSeconds() : time.getSeconds();
	return `${time.getFullYear()}.${(time.getMonth() + 1)}.${time.getDate()} ${hour}:${minute}:${second}`;
}

export { timestampToDateString, dateToString };
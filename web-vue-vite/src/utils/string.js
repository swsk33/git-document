// 用于字符串的实用函数

/**
 * 判断字符串是否为空或者空白
 * @param {string} value 传入字符串对象
 * @returns {boolean} 是否为空
 */
const isEmpty = (value) => {
	return value == null || value === '';
};

export { isEmpty };
/**
 * 文集状态对象构造函数
 * @param {String} name 状态名（后端获取的枚举名）
 * @param {String} showName 状态显示名（显示在页面的）
 * @param {String} tip 状态提示
 * @param {String} tipColor 状态提示颜色
 * @constructor
 */
function AnthologyStatus(name, showName, tip, tipColor) {
	this.name = name;
	this.showName = showName;
	this.tip = tip;
	this.tipColor = tipColor;
}

/**
 * 文集状态哈希表<br>
 * 键：状态名（后端传来的枚举）<br>
 * 值：状态对象
 */
const ANTHOLOGY_STATUS = new Map();

// 初始化哈希表
ANTHOLOGY_STATUS.set('UPDATING', new AnthologyStatus('UPDATING', '更新中', '该文集正在更新迭代', 'green'));
ANTHOLOGY_STATUS.set('ARCHIVE', new AnthologyStatus('ARCHIVE', '已归档', '该文集已不再更新且被归档，只能进行查看', 'red'));
ANTHOLOGY_STATUS.set('DEPRECATED', new AnthologyStatus('DEPRECATED', '已过时', '该文集中部分或者全部内容已被弃用，请谨慎参考', '#ff7217'));

export { ANTHOLOGY_STATUS };
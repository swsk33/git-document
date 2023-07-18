// 存放所有请求前缀的常量JS文件
import defaultAvatar from '../assets/avatar/default-avatar.jpg';
import defaultCover from '../assets/cover/default.png';

/**
 * 全部前缀
 */
const PREFIX = '/api/';

/**
 * 所有服务前缀
 */
const REQUEST_PREFIX = {
	/**
	 * 用户服务
	 */
	USER: PREFIX + 'user/',
	/**
	 * 文集仓库服务
	 */
	ANTHOLOGY: PREFIX + 'anthology/',
	/**
	 * 文章服务
	 */
	ARTICLE: PREFIX + 'article/',
	/**
	 * 公钥服务
	 */
	PUBLIC_KEY: PREFIX + 'public-key/',
	/**
	 * 收藏服务
	 */
	STAR: PREFIX + 'star/',
	/**
	 * 图片服务
	 */
	IMAGE: PREFIX + 'image/',
	/**
	 * 角色服务
	 */
	ROLE: PREFIX + 'role/',
	/**
	 * 用户偏好设置服务
	 */
	SETTING: PREFIX + 'setting/',
	/**
	 * 系统设置服务
	 */
	SYSTEM_SETTING: PREFIX + 'system-setting/',
	/**
	 * 邮件服务
	 */
	EMAIL: PREFIX + 'email/',
	/**
	 * 系统信息服务
	 */
	SYSTEM_INFO: PREFIX + 'system-info/'
};

/**
 * 解析图片资源的请求路径
 * @param filename 图片资源文件名
 */
const parseImageURL = (filename) => {
	return REQUEST_PREFIX.IMAGE + 'get/' + filename;
};

/**
 * 解析头像图片资源的请求路径
 * @param filename 图片资源文件名
 */
const parseAvatarURL = (filename) => {
	return filename == null ? defaultAvatar : parseImageURL(filename);
};

/**
 * 解析封面图片资源的请求路径
 * @param filename 图片资源文件名
 */
const parseCoverURL = (filename) => {
	return filename == null ? defaultCover : parseImageURL(filename);
};

export { REQUEST_PREFIX, parseImageURL, parseAvatarURL, parseCoverURL };
// 图片接口请求

import { REQUEST_METHOD, uploadFile } from '../utils/request.js';
import defaultAvatar from '../assets/avatar/default-avatar.jpg';
import defaultCover from '../assets/cover/default.png';

const apiPrefix = '/api/image';

/**
 * 上传图片
 * @param uploadName 上传文件的表单键名
 * @param uploadImage 上传的文件对象（从input元素获取）
 * @returns {Promise<Result<void>>} 响应体
 */
export async function imageUpload(uploadName, uploadImage) {
	const form = new FormData();
	form.append(uploadName, uploadImage);
	return uploadFile(`${apiPrefix}/upload`, REQUEST_METHOD.POST, form);
}

/**
 * 解析图片资源的请求路径
 * @param filename 图片资源文件名
 */
export function parseImageURL(filename) {
	return `${apiPrefix}/get/${filename}`;
}

/**
 * 解析头像图片资源的请求路径
 * @param filename 图片资源文件名
 */
export function parseAvatarURL(filename) {
	return filename == null ? defaultAvatar : parseImageURL(filename);
}

/**
 * 解析封面图片资源的请求路径
 * @param filename 图片资源文件名
 */
export function parseCoverURL(filename) {
	return filename == null ? defaultCover : parseImageURL(filename);
}
package com.gitee.swsk33.gitdocument.util;

import cn.dev33.satoken.secure.BCrypt;

/**
 * 密码加密实用类
 */
public class BCryptEncoder {

	/**
	 * 使用BCrypt进行加密
	 *
	 * @param originPassword 原始未加密密码
	 * @return 加密后密码
	 */
	public static String encode(String originPassword) {
		return BCrypt.hashpw(originPassword, BCrypt.gensalt());
	}

	/**
	 * 校验密码
	 *
	 * @param plainPassword   原始未加密明文密码
	 * @param encodedPassword 加密后的密码
	 * @return 原始密码是否和加密的密码相匹配
	 */
	public static boolean check(String plainPassword, String encodedPassword) {
		return BCrypt.checkpw(plainPassword, encodedPassword);
	}

}
package com.gitee.swsk33.gitdocument.service;

public interface UserEmailCodeService {

	/**
	 * 发送密码重置验证码
	 *
	 * @param email 需要重置密码的用户邮箱
	 */
	void sendPasswordResetCode(String email);

	/**
	 * 验证密码重置验证码
	 *
	 * @param userId 申请密码重置的用户id
	 * @param code   用户传入的验证码
	 * @return 是否验证成功
	 */
	boolean verifyPasswordResetCode(int userId, String code);

}
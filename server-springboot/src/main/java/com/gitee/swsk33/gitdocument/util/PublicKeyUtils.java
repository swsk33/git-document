package com.gitee.swsk33.gitdocument.util;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.common.config.keys.PublicKeyEntry;

import java.security.PublicKey;

/**
 * 关于公钥的实用类
 */
@Slf4j
public class PublicKeyUtils {

	/**
	 * 将PublicKey公钥对象转换成Base64字符串
	 * 通常公钥内容是以二进制编码存储，转换成Base64格式则以字符串形式存储
	 *
	 * @param publicKey 公钥对象
	 * @return 公钥Base64编码后字符串
	 */
	public static String publicKeyToBase64String(PublicKey publicKey) {
		return publicKey != null ? Base64.encode(publicKey.getEncoded()) : null;
	}

	/**
	 * 将OpenSSH格式的公钥字符串转换成PEM格式
	 * 需要注意的是，OpenSSH格式的公钥(id_rsa.pub)的内容编码和PEM格式的编码方式不同
	 * SSH服务端读取到用户发送的公钥是PEM格式，其内容和id_rsa.pub的内容会不一样
	 * 因此可以先把OpenSSH格式转换成PEM格式
	 *
	 * @param sshPublicKey OpenSSH格式的公钥内容，形如：ssh-rsa AAAAB3Nza... user@hostname
	 * @return PEM格式公钥内容，Base64形式，解析失败返回null
	 */
	public static String opensshPublicKeyToPEM(String sshPublicKey) {
		PublicKeyEntry entry = PublicKeyEntry.parsePublicKeyEntry(sshPublicKey);
		PublicKey publicKey = null;
		try {
			publicKey = entry.resolvePublicKey(null, null, null);
		} catch (Exception e) {
			log.error("转换公钥出现错误！{}", e.getMessage());
		}
		return publicKeyToBase64String(publicKey);
	}

}
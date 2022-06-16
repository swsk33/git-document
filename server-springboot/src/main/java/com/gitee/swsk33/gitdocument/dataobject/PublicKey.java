package com.gitee.swsk33.gitdocument.dataobject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户公钥信息
 */
@Setter
@Getter
@NoArgsConstructor
public class PublicKey implements Serializable {

	/**
	 * 主键id
	 */
	private int id;

	/**
	 * 所在文件行
	 */
	private int line;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 所属用户
	 */
	private User user;

	/**
	 * 创建时间
	 */
	private LocalDateTime gmtCreated;

	/**
	 * 修改时间
	 */
	private LocalDateTime gmtModified;

}
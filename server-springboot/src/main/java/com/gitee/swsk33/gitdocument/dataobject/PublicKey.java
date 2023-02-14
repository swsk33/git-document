package com.gitee.swsk33.gitdocument.dataobject;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户公钥信息
 */
@Data
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
	@NotEmpty(message = "公钥内容不能为空！")
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
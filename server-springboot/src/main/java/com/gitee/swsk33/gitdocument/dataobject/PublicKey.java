package com.gitee.swsk33.gitdocument.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户公钥信息
 */
@Data
@JsonIgnoreProperties(allowSetters = true, value = {"user"})
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
	@JsonIgnore
	private LocalDateTime gmtCreated;

	/**
	 * 修改时间
	 */
	@JsonIgnore
	private LocalDateTime gmtModified;

}
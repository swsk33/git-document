package com.gitee.swsk33.gitdocument.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户公钥信息
 */
@Data
@Table("public_key")
@JsonIgnoreProperties(allowSetters = true, value = {"userId", "content"})
public class PublicKey {

	/**
	 * 主键id
	 */
	@Id(keyType = KeyType.Auto)
	private Integer id;

	/**
	 * 公钥显示名称
	 */
	@NotEmpty(message = "公钥名称不可为空！")
	@Size(max = 16, message = "公钥名称不可超过16个字符！")
	private String name;

	/**
	 * 公钥内容，转换成PEM格式，Base64编码后再存入数据库
	 */
	@NotEmpty(message = "公钥内容不能为空！")
	private String content;

	/**
	 * 所属用户的id（外键）
	 */
	private Integer userId;

	/**
	 * 创建时间
	 */
	@Column(onInsertValue = "now()")
	private LocalDateTime gmtCreated;

	/**
	 * 修改时间
	 */
	@Column(onUpdateValue = "now()")
	private LocalDateTime gmtModified;

}
package com.gitee.swsk33.gitdocument.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户公钥信息
 */
@Data
@Table("public_key")
@JsonIgnoreProperties(allowSetters = true, value = {"userId"})
public class PublicKey {

	/**
	 * 主键id
	 */
	@Id(keyType = KeyType.Auto)
	private Integer id;

	/**
	 * 公钥内容
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
	private LocalDateTime gmtCreated;

	/**
	 * 修改时间
	 */
	private LocalDateTime gmtModified;

}
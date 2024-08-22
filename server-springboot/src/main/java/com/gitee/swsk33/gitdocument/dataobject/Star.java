package com.gitee.swsk33.gitdocument.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gitee.swsk33.gitdocument.param.ValidationRules;
import com.gitee.swsk33.gitdocument.serializer.LongToStringSerializer;
import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 星星（收藏）
 */
@Data
@Table("star")
@JsonIgnoreProperties(allowSetters = true, value = {"userId", "anthologyId"})
public class Star {

	/**
	 * 主键id
	 */
	@Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
	@JsonSerialize(using = LongToStringSerializer.class)
	@NotNull(groups = ValidationRules.DataUpdate.class, message = "星星id不能为空！")
	private Long id;

	/**
	 * 收藏的用户id（外键）
	 */
	private Integer userId;

	/**
	 * 被收藏的文集id（外键）
	 */
	@NotNull(groups = ValidationRules.DataAdd.class, message = "收藏的文集id不能为空！")
	private Long anthologyId;

	/**
	 * 被收藏的文集
	 */
	@RelationManyToOne(selfField = "anthologyId", targetField = "id")
	private Anthology anthology;

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
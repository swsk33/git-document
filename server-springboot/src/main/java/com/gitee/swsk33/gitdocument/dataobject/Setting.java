package com.gitee.swsk33.gitdocument.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gitee.swsk33.gitdocument.param.ValidationRules;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户偏好设置
 */
@Data
@Table("setting")
public class Setting implements Serializable {

	/**
	 * 主键id
	 */
	@Id(keyType = KeyType.Auto)
	@NotNull(groups = ValidationRules.DataUpdate.class, message = "设置id不能为空！")
	private Integer id;

	/**
	 * 在收藏的文集更新时是否接收通知
	 */
	private Boolean receiveUpdateEmail = true;

	/**
	 * 在有新的文集创建发布时是否接收通知
	 */
	private Boolean receiveNewEmail = true;

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
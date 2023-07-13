package com.gitee.swsk33.gitdocument.dataobject;

import com.gitee.swsk33.gitdocument.param.ValidationRules;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户偏好设置
 */
@Data
public class Setting implements Serializable {

	/**
	 * 主键id
	 */
	@NotNull(groups = ValidationRules.DataUpdate.class, message = "设置id不能为空！")
	private Integer id;

	/**
	 * 在收藏的文集更新时是否接收通知
	 */
	@NotNull(groups = ValidationRules.DataAdd.class, message = "设置项更新接收通知不能为空！")
	private Boolean receiveUpdateEmail;

	/**
	 * 在有新的文集创建发布时是否接收通知
	 */
	@NotNull(groups = ValidationRules.DataAdd.class, message = "设置项创建接收通知不能为空！")
	private Boolean receiveNewEmail;

	/**
	 * 创建时间
	 */
	private LocalDateTime gmtCreated;

	/**
	 * 修改时间
	 */
	private LocalDateTime gmtModified;

}
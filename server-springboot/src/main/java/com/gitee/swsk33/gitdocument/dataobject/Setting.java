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
	@NotNull(groups = ValidationRules.DataAdd.class, message = "设置项接收通知不能为空！")
	private Boolean receiveUpdateEmail;

	/**
	 * 创建时间
	 */
	private LocalDateTime gmtCreated;

	/**
	 * 修改时间
	 */
	private LocalDateTime gmtModified;

}
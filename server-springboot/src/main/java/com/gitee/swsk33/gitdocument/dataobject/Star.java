package com.gitee.swsk33.gitdocument.dataobject;

import com.gitee.swsk33.gitdocument.param.ValidationRules;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 星星（收藏）
 */
@Data
public class Star implements Serializable {

	/**
	 * 主键id
	 */
	@NotNull(groups = ValidationRules.DataUpdate.class, message = "星星id不能为空！")
	private Long id;

	/**
	 * 收藏的用户
	 */
	private User user;

	/**
	 * 被收藏的文集
	 */
	@NotEmpty(groups = ValidationRules.DataAdd.class, message = "收藏的文集不能为空！")
	private Anthology anthology;

	/**
	 * 创建时间
	 */
	private LocalDateTime gmtCreated;

	/**
	 * 修改时间
	 */
	private LocalDateTime gmtModified;

}
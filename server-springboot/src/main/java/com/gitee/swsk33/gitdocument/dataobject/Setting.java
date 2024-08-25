package com.gitee.swsk33.gitdocument.dataobject;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户偏好设置
 */
@Data
@Table("setting")
public class Setting {

	/**
	 * 设置对应的用户id
	 */
	@Id(keyType = KeyType.None)
	@NotNull(message = "设置的用户id不能为空！")
	private Integer userId;

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
	@Column(onInsertValue = "now()")
	private LocalDateTime gmtCreated;

	/**
	 * 修改时间
	 */
	@Column(onUpdateValue = "now()")
	private LocalDateTime gmtModified;

}
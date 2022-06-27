package com.gitee.swsk33.gitdocument.dataobject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色
 */
@Setter
@Getter
@NoArgsConstructor
public class Role implements Serializable {

	/**
	 * 主键id
	 */
	@NotNull(message = "角色id不能为空！")
	private Integer id;

	/**
	 * 名字
	 */
	private String name;

	/**
	 * 显示名
	 */
	private String showName;

	/**
	 * 该角色的权限
	 */
	private List<Permission> permissions;

	/**
	 * 创建时间
	 */
	private LocalDateTime gmtCreated;

	/**
	 * 修改时间
	 */
	private LocalDateTime gmtModified;

}
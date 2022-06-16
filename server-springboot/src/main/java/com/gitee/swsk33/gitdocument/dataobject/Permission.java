package com.gitee.swsk33.gitdocument.dataobject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限
 */
@Getter
@Setter
@NoArgsConstructor
public class Permission implements Serializable {

	/**
	 * 主键id
	 */
	private int id;

	/**
	 * 名字
	 */
	private String name;

	/**
	 * 拥有该权限的角色
	 */
	private List<Role> roles;

	/**
	 * 创建时间
	 */
	private LocalDateTime gmtCreated;

	/**
	 * 修改时间
	 */
	private LocalDateTime gmtModified;

}
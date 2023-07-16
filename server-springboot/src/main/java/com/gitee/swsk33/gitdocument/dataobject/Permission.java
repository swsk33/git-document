package com.gitee.swsk33.gitdocument.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限
 */
@Data
public class Permission implements Serializable {

	/**
	 * 主键id
	 */
	@NotNull(message = "权限id不能为空！")
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
	 * 拥有该权限的角色
	 */
	private List<Role> roles;

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
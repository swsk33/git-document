package com.gitee.swsk33.gitdocument.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色
 */
@Data
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
	@JsonIgnore
	private LocalDateTime gmtCreated;

	/**
	 * 修改时间
	 */
	@JsonIgnore
	private LocalDateTime gmtModified;

}
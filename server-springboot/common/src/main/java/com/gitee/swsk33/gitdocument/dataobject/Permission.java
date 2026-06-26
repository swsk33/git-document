package com.gitee.swsk33.gitdocument.dataobject;

import com.mybatisflex.annotation.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限
 */
@Data
@Table("permission")
public class Permission {

	/**
	 * 主键id
	 */
	@Id(keyType = KeyType.Auto)
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
	@RelationManyToMany(joinTable = "role_permission",
			selfField = "id", joinSelfColumn = "permission_id",
			targetField = "id", joinTargetColumn = "role_id")
	private List<Role> roles;

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
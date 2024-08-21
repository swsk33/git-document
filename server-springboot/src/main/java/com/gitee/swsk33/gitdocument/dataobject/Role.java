package com.gitee.swsk33.gitdocument.dataobject;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.RelationManyToMany;
import com.mybatisflex.annotation.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色
 */
@Data
@Table("role")
public class Role {

	/**
	 * 主键id
	 */
	@NotNull(message = "角色id不能为空！")
	@Id(keyType = KeyType.Auto)
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
	@RelationManyToMany(joinTable = "role_permission",
			selfField = "id", joinSelfColumn = "role_id",
			targetField = "id", joinTargetColumn = "permission_id")
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
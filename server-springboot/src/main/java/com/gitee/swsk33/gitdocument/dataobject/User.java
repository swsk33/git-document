package com.gitee.swsk33.gitdocument.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gitee.swsk33.gitdocument.param.ValidationRules;
import com.mybatisflex.annotation.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户类
 */
@Data
@Table("user")
@JsonIgnoreProperties(allowSetters = true, value = {"password", "roleId"})
public class User {

	/**
	 * 主键id
	 */
	@Id(keyType = KeyType.Auto)
	@Null(groups = ValidationRules.DataAdd.class, message = "新增数据时不能手动设定主键id！")
	@NotNull(groups = ValidationRules.DataUpdate.class, message = "用户id不能为空！")
	private Integer id;

	/**
	 * 用户名
	 */
	@NotEmpty(groups = {ValidationRules.DataAdd.class, ValidationRules.UserLogin.class}, message = "用户名不能为空！")
	@Size(groups = {ValidationRules.DataAdd.class, ValidationRules.DataUpdate.class}, max = 16, message = "用户名长度不能大于32！")
	@Pattern(groups = {ValidationRules.DataAdd.class, ValidationRules.DataUpdate.class}, regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9_]+$", message = "用户名只能是由中文、英文、数字或者下划线组成！")
	private String username;

	/**
	 * 密码
	 */
	@NotEmpty(groups = {ValidationRules.DataAdd.class, ValidationRules.UserLogin.class, ValidationRules.UserPasswordReset.class}, message = "密码不能为空！")
	@Size(groups = {ValidationRules.DataAdd.class, ValidationRules.DataUpdate.class}, min = 8, message = "密码长度不能小于8位！")
	private String password;

	/**
	 * 昵称
	 */
	@NotEmpty(groups = ValidationRules.DataAdd.class, message = "昵称不能为空！")
	@Size(groups = {ValidationRules.DataAdd.class, ValidationRules.DataUpdate.class}, max = 32, message = "昵称长度不能超过32！")
	private String nickname;

	/**
	 * 头像文件名，为null时表示使用默认头像
	 */
	private String avatar;

	/**
	 * 邮箱
	 */
	@Email(groups = {ValidationRules.DataAdd.class, ValidationRules.DataUpdate.class}, message = "邮箱格式不正确！")
	@NotEmpty(groups = {ValidationRules.DataAdd.class, ValidationRules.UserPasswordReset.class}, message = "邮箱不能为空！")
	private String email;

	/**
	 * 角色id（外键）
	 */
	@NotNull(groups = ValidationRules.DataAdd.class, message = "用户的角色id不能为空！")
	private Integer roleId;

	/**
	 * 角色
	 */
	@RelationManyToOne(selfField = "roleId", targetField = "id")
	private Role role;

	/**
	 * 用户上一次登录记录
	 */
	@RelationOneToOne(selfField = "id", targetField = "userId")
	private LoginRecord loginRecord;

	/**
	 * 偏好设置
	 */
	@RelationOneToOne(selfField = "id", targetField = "userId")
	private Setting setting;

	/**
	 * 用户的收藏
	 */
	@RelationOneToMany(selfField = "id", targetField = "userId")
	private List<Star> stars;

	/**
	 * 用户的公钥
	 */
	@RelationOneToMany(selfField = "id", targetField = "userId")
	private List<PublicKey> keys;

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
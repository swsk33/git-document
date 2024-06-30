package com.gitee.swsk33.gitdocument.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gitee.swsk33.gitdocument.param.ValidationRules;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户类
 */
@Data
@Table("user")
@JsonIgnoreProperties(allowSetters = true, value = {"password"})
public class User implements Serializable {

	/**
	 * 主键id
	 */
	@Id(keyType = KeyType.Auto)
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
	 * 偏好设置
	 */
	private Setting setting;

	/**
	 * 角色
	 */
	@NotNull(groups = ValidationRules.DataAdd.class, message = "角色不能为空！")
	private Role role;

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
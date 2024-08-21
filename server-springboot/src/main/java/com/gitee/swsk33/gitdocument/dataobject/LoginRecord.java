package com.gitee.swsk33.gitdocument.dataobject;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户登录记录
 */
@Data
@Table("login_record")
public class LoginRecord {

	/**
	 * 关联的用户id
	 */
	@Id(keyType = KeyType.None)
	private Integer userId;

	/**
	 * 上一次登录的IP地址
	 */
	private String ip;

	/**
	 * 上一次登录位置
	 */
	private String location;

	/**
	 * 创建时间
	 */
	private LocalDateTime gmtCreated;

	/**
	 * 修改时间
	 */
	private LocalDateTime gmtModified;

}
package com.gitee.swsk33.gitdocument.dataobject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 星星（收藏）
 */
@Setter
@Getter
@NoArgsConstructor
public class Star implements Serializable {

	/**
	 * 主键id
	 */
	private long id;

	/**
	 * 收藏的用户
	 */
	private User user;

	/**
	 * 被收藏的文集
	 */
	private Anthology anthology;

}
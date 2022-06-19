package com.gitee.swsk33.gitdocument.dataobject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章集
 */
@Setter
@Getter
@NoArgsConstructor
public class Anthology implements Serializable {

	/**
	 * 主键id
	 */
	private long id;

	/**
	 * 文集名
	 */
	private String name;

	/**
	 * 显示名
	 */
	private String showName;

	/**
	 * 封面
	 */
	private String cover;

	/**
	 * 对应仓库地址
	 */
	private String repoPath;

	/**
	 * 最后一次的提交id，新仓库为null
	 */
	private String latestCommitId;

	/**
	 * 创建时间
	 */
	private LocalDateTime gmtCreated;

	/**
	 * 修改时间
	 */
	private LocalDateTime gmtModified;

}
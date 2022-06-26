package com.gitee.swsk33.gitdocument.dataobject;

import com.gitee.swsk33.gitdocument.param.ValidationRules;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
	@NotNull(groups = ValidationRules.DataUpdate.class, message = "文集id不能为空！")
	private Long id;

	/**
	 * 文集名
	 */
	@NotNull(groups = ValidationRules.DataAdd.class, message = "文集名称不能为空！")
	@Size(groups = {ValidationRules.DataAdd.class, ValidationRules.DataUpdate.class}, max = 64, message = "文集名称长度不能超过64！")
	@Pattern(groups = {ValidationRules.DataAdd.class, ValidationRules.DataUpdate.class}, regexp = "^[0-9a-zA-Z_]+$", message = "文集名只能是由英文字母、数字和下划线组成！")
	private String name;

	/**
	 * 显示名
	 */
	@NotNull(groups = ValidationRules.DataAdd.class, message = "文集显示名不能为空！")
	@Size(max = 64, message = "文集显示名长度不能超过64！")
	private String showName;

	/**
	 * 封面
	 */
	private String cover;

	/**
	 * 当前运行用户的用户名（用于拼接克隆文集仓库地址）
	 */
	private String systemUser;

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
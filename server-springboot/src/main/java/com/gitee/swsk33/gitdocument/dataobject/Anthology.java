package com.gitee.swsk33.gitdocument.dataobject;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gitee.swsk33.gitdocument.param.ValidationRules;
import com.gitee.swsk33.gitdocument.serializer.LongToStringSerializer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章集
 */
@Data
public class Anthology implements Serializable {

	/**
	 * 主键id
	 */
	@JsonSerialize(using = LongToStringSerializer.class)
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
	 * 封面图片文件名，为null时表示使用默认封面
	 */
	private String cover;

	/**
	 * 当前运行用户的用户名（用于拼接克隆文集仓库地址）
	 */
	private String systemUser;

	/**
	 * 文集仓库暴露Git SSH宿主机端口（用于拼接克隆文集仓库地址）
	 */
	private Integer sshPort;

	/**
	 * 文集最后更新的时间戳
	 */
	private Integer updateTime;

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
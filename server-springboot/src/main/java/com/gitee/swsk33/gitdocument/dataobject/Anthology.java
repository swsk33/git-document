package com.gitee.swsk33.gitdocument.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gitee.swsk33.gitdocument.param.AnthologyStatus;
import com.gitee.swsk33.gitdocument.param.ValidationRules;
import com.gitee.swsk33.gitdocument.serializer.LongToStringSerializer;
import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章集
 */
@Data
@Table("anthology")
@JsonIgnoreProperties(allowSetters = true, value = {"latestCommit"})
public class Anthology {

	/**
	 * 主键id
	 */
	@Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
	@JsonSerialize(using = LongToStringSerializer.class)
	@Null(groups = ValidationRules.DataAdd.class, message = "新增数据时不能手动设定主键id！")
	@NotNull(groups = ValidationRules.DataUpdate.class, message = "文集id不能为空！")
	private Long id;

	/**
	 * 文集名
	 */
	@NotEmpty(groups = ValidationRules.DataAdd.class, message = "文集名称不能为空！")
	@Size(groups = {ValidationRules.DataAdd.class, ValidationRules.DataUpdate.class}, max = 64, message = "文集名称长度不能超过64！")
	@Pattern(groups = {ValidationRules.DataAdd.class, ValidationRules.DataUpdate.class}, regexp = "^[0-9a-zA-Z_]+$", message = "文集名只能是由英文字母、数字和下划线组成！")
	private String name;

	/**
	 * 显示名
	 */
	@NotEmpty(groups = ValidationRules.DataAdd.class, message = "文集显示名不能为空！")
	@Size(max = 64, message = "文集显示名长度不能超过64！")
	private String showName;

	/**
	 * 封面图片文件名，为null时表示使用默认封面
	 */
	private String cover;

	/**
	 * 文集最后更新的时间戳
	 */
	@Column(ignore = true)
	private Integer updateTime;

	/**
	 * 对应仓库地址
	 */
	private String repoPath;

	/**
	 * 最后一次的Git Commit提交id，新仓库为null
	 */
	@Null(groups = {ValidationRules.DataAdd.class, ValidationRules.DataUpdate.class}, message = "请勿手动设定或修改commitId！")
	private String latestCommit;

	/**
	 * 文集仓库状态
	 */
	private AnthologyStatus status;

	/**
	 * 该文集所包含的文章列表
	 */
	@RelationOneToMany(selfField = "id", targetField = "anthologyId")
	private List<Article> articles;

	/**
	 * 这个文集的收藏列表
	 */
	@RelationOneToMany(selfField = "id", targetField = "anthologyId")
	private List<Star> stars;

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
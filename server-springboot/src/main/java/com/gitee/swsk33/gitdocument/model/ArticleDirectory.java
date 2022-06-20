package com.gitee.swsk33.gitdocument.model;

import com.gitee.swsk33.gitdocument.param.FileIndexType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 文章目录索引，类似系统目录一样的树状结构，用于返回给前端作为树状结构显示和解析
 */
@Getter
@Setter
public class ArticleDirectory implements Serializable {

	/**
	 * 索引类型为目录
	 */
	private final FileIndexType type = FileIndexType.DIR;

	/**
	 * 目录名
	 */
	private String name;

	/**
	 * 子目录
	 */
	private List<ArticleDirectory> directories = new ArrayList<>();

	/**
	 * 文章列表
	 */
	private List<ArticleFile> articles = new ArrayList<>();

	/**
	 * 通过目录名获取该目录下的目录对象引用
	 *
	 * @param directoryName 目录名
	 * @return 对应的目录对象，若不存在返回null
	 */
	public ArticleDirectory getDirectoryByName(String directoryName) {
		for (ArticleDirectory directory : directories) {
			if (directory.getName().equals(directoryName)) {
				return directory;
			}
		}
		return null;
	}

}
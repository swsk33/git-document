package com.gitee.swsk33.gitdocument.model;

import com.gitee.swsk33.gitdocument.dataobject.Article;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 文章目录索引，类似系统目录一样的树状结构，用于返回给前端作为树状结构显示和解析
 */
@Data
@NoArgsConstructor
public class ArticleDirectory implements Serializable {

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
	 * 用文章列表来创建该目录结构（将文章对象中路径取出，并将其扁平路径转为树状）
	 *
	 * @param articles 文章列表
	 */
	public ArticleDirectory(List<Article> articles) {
		for (Article article : articles) {
			String[] paths = article.getFilePath().split("/");
			// 目录指针，用于标识当前遍历的时候进入到了哪个目录中
			ArticleDirectory pointer = this;
			for (int i = 0; i < paths.length; i++) {
				if (i == paths.length - 1) {
					ArticleFile file = new ArticleFile();
					file.setId(article.getId());
					file.setName(paths[i].substring(0, paths[i].lastIndexOf(".")));
					pointer.getArticles().add(file);
					break;
				}
				if (pointer.getDirectoryByName(paths[i]) == null) {
					ArticleDirectory directory = new ArticleDirectory();
					directory.setName(paths[i]);
					pointer.getDirectories().add(directory);
				}
				// 指针指向下一级目录
				pointer = pointer.getDirectoryByName(paths[i]);
			}
		}
	}

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
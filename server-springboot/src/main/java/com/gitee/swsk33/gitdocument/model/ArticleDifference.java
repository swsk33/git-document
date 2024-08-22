package com.gitee.swsk33.gitdocument.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.jgit.diff.DiffEntry;

import java.io.Serializable;
import java.util.List;

/**
 * 自定义文章Git差异信息<br>
 * 用于表示文集仓库中两次commit的中的一条差异变化
 */
@Data
@NoArgsConstructor
public class ArticleDifference implements Serializable {

	/**
	 * 差异改变类型
	 */
	private DiffEntry.ChangeType changeType;

	/**
	 * 差异文件新路径
	 */
	private String newPath;

	/**
	 * 差异文件旧路径
	 */
	private String oldPath;

	/**
	 * 以JGit差异对象构建该文章差异对象
	 *
	 * @param entry JGit的差异对象
	 */
	public ArticleDifference(DiffEntry entry) {
		this.changeType = entry.getChangeType();
		this.newPath = entry.getNewPath();
		this.oldPath = entry.getOldPath();
	}

	/**
	 * 从JGit的差异对象列表批量转换为文章差异对象
	 *
	 * @param diffEntries JGit差异对象列表
	 * @return 文章差异对象
	 */
	public static List<ArticleDifference> toArticleDiff(List<DiffEntry> diffEntries) {
		return diffEntries.stream().map(ArticleDifference::new).toList();
	}

	@Override
	public String toString() {
		return switch (changeType) {
			case ADD -> "增加了文章：" + newPath;
			case DELETE -> "删除了文章：" + oldPath;
			case RENAME -> "重命名或者移动了文章：" + oldPath + " -> " + newPath;
			case MODIFY -> "修改了文章内容：" + newPath;
			default -> "未知修改！";
		};
	}

}
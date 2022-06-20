package com.gitee.swsk33.gitdocument.util;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;

/**
 * Git仓库实用类
 */
public class GitRepositoryUtils {

	/**
	 * 创建一个Git裸仓库
	 *
	 * @param dirPath 仓库文件夹位置
	 * @return 是否创建成功
	 */
	public static boolean initGitBareRepository(String dirPath) {
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		try {
			Git.init().setDirectory(dir).setBare(true).call();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return dir.exists();
	}

	/**
	 * 获得一个仓库头指针的提交id（通常是最新提交）
	 *
	 * @param repositoryPath 仓库位置
	 * @return 仓库头指针所在的commit的id，若仓库中没有任何提交则返回null
	 */
	public static String getHeadCommitId(String repositoryPath) throws Exception {
		// 读取现有仓库
		Repository repository = new FileRepositoryBuilder().setGitDir(new File(repositoryPath)).build();
		// 得到HEAD指针的提交ID
		ObjectId headCommitId = repository.resolve("HEAD");
		if (headCommitId == null) {
			return null;
		}
		return headCommitId.getName();
	}

}
package com.gitee.swsk33.gitdocument.util;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
	 * 获取一个仓库最新的提交对象
	 *
	 * @param repositoryPath 仓库位置
	 * @return 仓库头指针所在的commit对象
	 */
	public static RevCommit getHeadCommit(String repositoryPath) throws Exception {
		// 读取现有仓库
		Repository repository = new FileRepositoryBuilder().setGitDir(new File(repositoryPath)).build();
		ObjectId headId = repository.resolve("HEAD");
		if (headId == null) {
			return null;
		}
		return repository.parseCommit(headId);
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

	/**
	 * 获取一个仓库全部的提交对象
	 *
	 * @param repositoryPath 仓库位置
	 * @return 全部提交对象列表
	 */
	public static List<RevCommit> getAllCommits(String repositoryPath) throws Exception {
		List<RevCommit> result = new ArrayList<>();
		// 读取现有仓库
		Repository repository = new FileRepositoryBuilder().setGitDir(new File(repositoryPath)).build();
		// 创建修订对象，可以用于获取所有的commit
		RevWalk walk = new RevWalk(repository);
		// 设定从HEAD指针为起始点开始遍历
		walk.markStart(walk.parseCommit(repository.resolve("HEAD")));
		// 遍历获取信息，时间从新到旧
		for (RevCommit commit : walk) {
			result.add(commit);
		}
		return result;
	}

}
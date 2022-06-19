package com.gitee.swsk33.gitdocument.util;

import com.gitee.swsk33.gitdocument.context.GitTaskContext;
import com.gitee.swsk33.gitdocument.model.GitCreateTask;
import com.gitee.swsk33.gitdocument.model.GitUpdateTask;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
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
	 * 新建一个仓库分支时，触发的任务
	 *
	 * @param repositoryId   仓库id
	 * @param repositoryPath 被监听的仓库
	 */
	public static void repositoryCreateUpdate(long repositoryId, String repositoryPath) throws Exception {
		GitCreateTask task = new GitCreateTask(repositoryId, GitFileUtils.getLatestFileList(repositoryPath));
		GitTaskContext.taskQueue.offer(task);
	}

	/**
	 * 仓库变化时的触发任务
	 *
	 * @param repositoryId   仓库id
	 * @param repositoryPath 被监听的仓库
	 */
	public static void repositoryChangeUpdate(long repositoryId, String repositoryPath) throws Exception {
		// 这里从数据库获取
		String oldId = "";
		// 获取新的commit id
		String newId = getHeadCommitId(repositoryPath);
		if (oldId.equals(newId)) {
			return;
		}
		// 获取差异
		List<DiffEntry> diffs = GitFileUtils.compareDiffBetweenTwoCommits(repositoryPath, oldId, newId);
		if (diffs.size() == 0) {
			return;
		}
		// 组装任务放到任务队列
		GitUpdateTask task = new GitUpdateTask(repositoryId, newId, diffs);
		GitTaskContext.taskQueue.offer(task);
	}

}
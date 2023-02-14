package com.gitee.swsk33.gitdocument.util;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Git仓库文件实用类
 */
public class GitFileUtils {

	/**
	 * 获取一个git工作区的最新的所有markdown文件列表
	 *
	 * @param gitDirectory Git版本库目录（.git文件夹）
	 * @return 最新版本的版本库中所有文件列表
	 */
	public static List<String> getLatestFileList(String gitDirectory) throws Exception {
		List<String> filePaths = new ArrayList<>();
		// 读取现有仓库
		Repository repository = new FileRepositoryBuilder().setGitDir(new File(gitDirectory)).build();
		// 得到HEAD指针的提交ID
		ObjectId headCommitId = repository.resolve("HEAD");
		// 创建修订对象，可以用于获取所有的commit
		RevWalk walk = new RevWalk(repository);
		// 得到HEAD的commit（最新的commit）
		RevCommit commit = walk.parseCommit(headCommitId);
		// 得到文件树
		RevTree tree = commit.getTree();
		// 用一个TreeWalk对象去迭代该仓库文件树中所有的文件
		TreeWalk treeWalk = new TreeWalk(repository);
		treeWalk.addTree(tree);
		// 设定递归获取
		treeWalk.setRecursive(true);
		// 迭代文件列表
		while (treeWalk.next()) {
			// 排除不是markdown的文件
			if (!treeWalk.getPathString().endsWith(".md")) {
				continue;
			}
			filePaths.add(treeWalk.getPathString());
		}
		return filePaths;
	}

	/**
	 * 获取Git仓库中最新提交的版本中某个文件的二进制字节内容
	 *
	 * @param gitDirectory Git版本库目录（.git文件夹）
	 * @param filePath     文件路径（相对于工作区下的路径）
	 * @return 文件内容，为字节数组形式
	 */
	public static byte[] getFileBytesInLatestCommit(String gitDirectory, String filePath) throws Exception {
		// 读取现有仓库
		Repository repository = new FileRepositoryBuilder().setGitDir(new File(gitDirectory)).build();
		// 得到HEAD指针的提交ID
		ObjectId headCommitId = repository.resolve("HEAD");
		// 创建修订对象，可以用于获取所有的commit
		RevWalk walk = new RevWalk(repository);
		// 得到HEAD的commit（最新的commit）
		RevCommit commit = walk.parseCommit(headCommitId);
		// 得到commit中文件树中某个具体的文件
		TreeWalk treeWalk = TreeWalk.forPath(repository, filePath, commit.getTree());
		// 得到其ObjectId（blob表示存放文件内容的对象）
		ObjectId blobId = treeWalk.getObjectId(0);
		// 创建对象读取器
		ObjectReader objectReader = repository.newObjectReader();
		// 利用对象加载器打开对应的blob对象（即为文件）
		ObjectLoader objectLoader = objectReader.open(blobId);
		// 得到其内容字节数据
		return objectLoader.getBytes();
	}

	/**
	 * 获取Git仓库中最新提交的版本中某个文件的文本内容
	 *
	 * @param gitDirectory Git版本库目录（.git文件夹）
	 * @param filePath     文件路径（相对于工作区下的路径）
	 * @return 文件内容，为字符串形式
	 */
	public static String getFileTextContentInLatestCommit(String gitDirectory, String filePath) throws Exception {
		return new String(getFileBytesInLatestCommit(gitDirectory, filePath), StandardCharsets.UTF_8);
	}

	/**
	 * 比较两次仓库的文件变化
	 *
	 * @param repositoryPath 仓库位置
	 * @param oldCommitId    旧的提交Id
	 * @param newCommitId    新的提交Id
	 * @return 变化对象，只返回增加、删除和修改文件名这几种变化
	 */
	public static List<DiffEntry> compareDiffBetweenTwoCommits(String repositoryPath, String oldCommitId, String newCommitId) throws Exception {
		// 初始化仓库
		Repository repository = new FileRepositoryBuilder().setGitDir(new File(repositoryPath)).build();
		// 新建修订版本图对象
		RevWalk walk = new RevWalk(repository);
		// 新建对象读取器
		ObjectReader objectReader = repository.newObjectReader();
		// 旧的工作树
		CanonicalTreeParser oldTree = new CanonicalTreeParser();
		RevCommit oldCommit = walk.parseCommit(repository.resolve(oldCommitId));
		oldTree.reset(objectReader, walk.parseTree(oldCommit.getTree()));
		// 新的工作树
		CanonicalTreeParser newTree = new CanonicalTreeParser();
		RevCommit newCommit = walk.parseCommit(repository.resolve(newCommitId));
		newTree.reset(objectReader, walk.parseTree(newCommit));
		// 执行diff命令
		Git git = new Git(repository);
		List<DiffEntry> origin = git.diff().setShowNameAndStatusOnly(true).setOldTree(oldTree).setNewTree(newTree).call();
		List<DiffEntry> result = new ArrayList<>();
		for (DiffEntry entry : origin) {
			if (entry.getChangeType() == DiffEntry.ChangeType.MODIFY || entry.getChangeType() == DiffEntry.ChangeType.COPY) {
				continue;
			}
			result.add(entry);
		}
		return result;
	}

}
package com.gitee.swsk33.gitdocument.git.impl;

import cn.hutool.core.io.FileUtil;
import com.gitee.swsk33.gitdocument.git.GitFileDAO;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.DiffCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.gitee.swsk33.gitdocument.param.CommonValue.GIT_HEAD_POINTER;

@Slf4j
@Component
public class GitFileDAOImpl implements GitFileDAO {

	@Override
	public List<String> getFileList(String gitDirectory, String commitId) {
		List<String> filePaths = new ArrayList<>();
		// 读取现有仓库
		try (Repository repository = new FileRepositoryBuilder().setGitDir(FileUtil.file(gitDirectory)).build()) {
			// 根据传入commitId字符串解析commit的ObjectId对象
			ObjectId commitObjectId = repository.resolve(commitId);
			if (commitObjectId == null) {
				log.warn("传入的commitId或者ref：{}有误！可能不存在于仓库{}中！", commitId, gitDirectory);
				return filePaths;
			}
			// 得到commit对象
			RevCommit commit = repository.parseCommit(commitObjectId);
			// 用一个TreeWalk对象去迭代该仓库文件树中所有的文件
			TreeWalk treeWalk = new TreeWalk(repository);
			// 获取最新commit中的文件树
			treeWalk.addTree(commit.getTree());
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
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return filePaths;
	}

	@Override
	public List<String> getLatestFileList(String gitDirectory) {
		return getFileList(gitDirectory, GIT_HEAD_POINTER);
	}

	@Override
	public byte[] getFileBytes(String gitDirectory, String filePath, String commitId) {
		byte[] data;
		// 读取现有仓库并创建对象读取器
		try (Repository repository = new FileRepositoryBuilder().setGitDir(FileUtil.file(gitDirectory)).build(); ObjectReader objectReader = repository.newObjectReader()) {
			// 根据传入commitId字符串解析commit的ObjectId对象
			ObjectId commitObjectId = repository.resolve(commitId);
			if (commitObjectId == null) {
				log.warn("传入的commitId或者ref：{}有误！可能不存在于仓库{}中！", commitId, gitDirectory);
				return null;
			}
			// 得到commit对象
			RevCommit commit = repository.parseCommit(commitObjectId);
			// 得到commit中文件树中某个具体的文件
			TreeWalk treeWalk = TreeWalk.forPath(repository, filePath, commit.getTree());
			// 得到这个blob对象的ObjectId（blob表示存放文件内容的对象）
			ObjectId blobId = treeWalk.getObjectId(0);
			// 利用对象加载器打开对应的blob对象（即为文件）
			ObjectLoader objectLoader = objectReader.open(blobId);
			// 读取内容
			data = objectLoader.getBytes();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return data;
	}

	@Override
	public byte[] getFileBytesInLatestCommit(String gitDirectory, String filePath) {
		return getFileBytes(gitDirectory, filePath, GIT_HEAD_POINTER);
	}

	@Override
	public String getFileTextContentInLatestCommit(String gitDirectory, String filePath) {
		return new String(getFileBytesInLatestCommit(gitDirectory, filePath), StandardCharsets.UTF_8);
	}

	@Override
	public List<DiffEntry> compareDiffBetweenTwoCommits(String gitDirectory, String oldCommitId, String newCommitId) {
		List<DiffEntry> result = new ArrayList<>();
		// 初始化仓库并新建对象读取器
		try (Repository repository = new FileRepositoryBuilder().setGitDir(FileUtil.file(gitDirectory)).build(); ObjectReader objectReader = repository.newObjectReader()) {
			// 获取旧的工作树
			CanonicalTreeParser oldTree = new CanonicalTreeParser();
			RevCommit oldCommit = repository.parseCommit(repository.resolve(oldCommitId));
			oldTree.reset(objectReader, oldCommit.getTree().getId());
			// 新的工作树
			CanonicalTreeParser newTree = new CanonicalTreeParser();
			RevCommit newCommit = repository.parseCommit(repository.resolve(newCommitId));
			newTree.reset(objectReader, newCommit.getTree().getId());
			// 执行diff命令
			DiffCommand diffCommand = new Git(repository).diff();
			diffCommand.setShowNameAndStatusOnly(true);
			diffCommand.setOldTree(oldTree);
			diffCommand.setNewTree(newTree);
			List<DiffEntry> diffs = diffCommand.call();
			diffs.stream().filter(diff -> diff.getChangeType() != DiffEntry.ChangeType.COPY).forEach(result::add);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

}
package com.gitee.swsk33.gitdocument.gitdao.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.gitee.swsk33.gitdocument.annotation.GitRepository;
import com.gitee.swsk33.gitdocument.broker.GitMessageBroker;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.gitdao.GitCommitDAO;
import com.gitee.swsk33.gitdocument.gitdao.GitFileDAO;
import com.gitee.swsk33.gitdocument.gitdao.GitRepositoryDAO;
import com.gitee.swsk33.gitdocument.model.ArticleDifference;
import com.gitee.swsk33.gitdocument.model.GitCreateTaskMessage;
import com.gitee.swsk33.gitdocument.model.GitUpdateTaskMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;

@Slf4j
@GitRepository
public class GitRepositoryDAOImpl implements GitRepositoryDAO {

	@Autowired
	private GitCommitDAO gitCommitDAO;

	@Autowired
	private GitFileDAO gitFileDAO;

	@Resource
	private GitMessageBroker messageBroker;

	@Override
	public boolean createGitBareRepository(String gitRepository) {
		// 裸仓库文件夹
		File repositoryFolder = FileUtil.file(gitRepository);
		// 创建仓库文件夹
		FileUtil.mkdir(repositoryFolder);
		try {
			// 初始化裸仓库
			Git.init().setDirectory(repositoryFolder).setBare(true).call().close();
		} catch (Exception e) {
			log.error("创建裸仓库出错！");
			log.error(e.getMessage());
			return false;
		}
		return repositoryFolder.exists();
	}

	@Override
	public void doCreateTask(String repoPath, String newId) {
		log.info("Git HEAD被创建！");
		GitCreateTaskMessage createTaskMessage = new GitCreateTaskMessage();
		createTaskMessage.setRepoPath(repoPath);
		createTaskMessage.setCommitId(newId);
		createTaskMessage.setFileList(gitFileDAO.getLatestFileList(repoPath));
		messageBroker.publish(createTaskMessage);
		log.info("已发布Git仓库创建任务消息至Flux对象！");
	}

	@Override
	public void doUpdateTask(String repoPath, String oldId, String newId, boolean sendEmail) {
		log.info("Git HEAD被修改！");
		if (StrUtil.isEmpty(newId) || newId.equals(oldId)) {
			log.info("实际没有变化，无需修改！");
			return;
		}
		// 获取差异
		List<DiffEntry> diffs = gitFileDAO.compareDiffBetweenTwoCommits(repoPath, oldId, newId);
		if (diffs.isEmpty()) {
			log.info("没有需要更新的差异！");
			return;
		}
		// 组装任务消息对象
		GitUpdateTaskMessage updateTaskMessage = new GitUpdateTaskMessage();
		updateTaskMessage.setRepoPath(repoPath);
		updateTaskMessage.setCommitId(newId);
		updateTaskMessage.setDiffs(ArticleDifference.toArticleDiff(diffs));
		updateTaskMessage.setSendEmail(sendEmail);
		messageBroker.publish(updateTaskMessage);
		log.info("已发布Git仓库更新任务消息至Flux对象！");
	}

	@Override
	public void checkGitRepositoryUpdate(Anthology anthology) {
		// 获取本地仓库的commit信息
		String localCommitId = gitCommitDAO.getHeadCommitId(anthology.getRepoPath());
		// 如果说本地仓库commit为空，说明是刚创建的仓库，则不进行对比
		if (StrUtil.isEmpty(localCommitId)) {
			log.warn("本地仓库：{}的commitId为空！可能是刚创建的仓库，跳过比对！", anthology.getName());
			return;
		}
		// 如果数据库中commit为空，说明需要执行创建任务
		if (StrUtil.isEmpty(anthology.getLatestCommit())) {
			log.warn("发现本地仓库：{}在数据库中的commitId为空，进行创建操作...", anthology.getName());
			doCreateTask(anthology.getRepoPath(), localCommitId);
			return;
		}
		// 如果只是单纯的两者不同，说明需要进行更新同步操作
		if (!anthology.getLatestCommit().equals(localCommitId)) {
			log.warn("发现本地仓库：{}与数据库的commit不同，进行更新操作...", anthology.getName());
			doUpdateTask(anthology.getRepoPath(), anthology.getLatestCommit(), localCommitId, false);
		}
	}

}
package com.gitee.swsk33.gitdocument.git.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import com.gitee.swsk33.gitdocument.dao.SystemSettingDAO;
import com.gitee.swsk33.gitdocument.dao.UserDAO;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.git.GitCommitDAO;
import com.gitee.swsk33.gitdocument.git.GitFileDAO;
import com.gitee.swsk33.gitdocument.git.GitRepositoryInfoDAO;
import com.gitee.swsk33.gitdocument.message.GitCreateTaskMessage;
import com.gitee.swsk33.gitdocument.message.GitUpdateTaskMessage;
import com.gitee.swsk33.gitdocument.message.UpdateEmailMessage;
import com.gitee.swsk33.gitdocument.model.ArticleDiff;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static com.gitee.swsk33.gitdocument.param.RabbitMessageQueue.Exchange.EMAIL_TOPIC_EXCHANGE;
import static com.gitee.swsk33.gitdocument.param.RabbitMessageQueue.Exchange.GIT_TASK_TOPIC_EXCHANGE;
import static com.gitee.swsk33.gitdocument.param.RabbitMessageQueue.RoutingKey.*;
import static com.gitee.swsk33.gitdocument.param.SystemSettingKey.ORGANIZATION_NAME;

@Slf4j
@Component
public class GitRepositoryInfoDAOImpl implements GitRepositoryInfoDAO {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private SystemSettingDAO systemSettingDAO;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private GitCommitDAO gitCommitDAO;

	@Autowired
	private GitFileDAO gitFileDAO;


	@Override
	public boolean initGitBareRepository(String gitRepository) {
		// 裸仓库文件夹
		File repositoryFolder = FileUtil.file(gitRepository);
		// 钩子脚本所在位置
		File hookScript = FileUtil.file(gitRepository + File.separator + "hooks/post-receive");
		// 创建仓库文件夹
		FileUtil.mkdir(repositoryFolder);
		// 创建钩子文件的输入输出流，完成把钩子脚本模板内容读取并写入到仓库文件夹指定位置
		try (InputStream hookStream = new ClassPathResource("/script/git-message.sh").getStream()) {
			// 初始化裸仓库
			Git.init().setDirectory(repositoryFolder).setBare(true).call().close();
			// 创建钩子脚本文件并赋予脚本可执行权限
			FileUtil.touch(hookScript);
			RuntimeUtil.exec("chmod +x " + hookScript.getAbsolutePath());
			// 读取模板文件内容
			byte[] content = hookStream.readAllBytes();
			// 输出流接收模板文件内容并写入
			try (OutputStream scriptOutput = new FileOutputStream(hookScript)) {
				// 写入至仓库中钩子文件
				scriptOutput.write(content);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return repositoryFolder.exists();
	}

	@Override
	public void doCreateTask(long id, String gitRepository, String newId) {
		log.info("Git HEAD被创建！");
		GitCreateTaskMessage createTaskMessage = new GitCreateTaskMessage();
		createTaskMessage.setRepositoryId(id);
		createTaskMessage.setCommitId(newId);
		createTaskMessage.setFileList(gitFileDAO.getLatestFileList(gitRepository));
		rabbitTemplate.convertAndSend(GIT_TASK_TOPIC_EXCHANGE, GIT_CREATE, createTaskMessage);
		log.info("已发布Git仓库创建任务消息至消息队列！");
	}

	@Override
	public void doUpdateTask(long id, String showName, String gitRepository, boolean sendEmail, String oldId, String newId) {
		log.info("Git HEAD被修改！");
		if (StrUtil.isEmpty(newId) || newId.equals(oldId)) {
			log.info("实际没有变化，无需修改！");
			return;
		}
		// 获取差异
		List<DiffEntry> diffs = gitFileDAO.compareDiffBetweenTwoCommits(gitRepository, oldId, newId);
		if (diffs.isEmpty()) {
			log.info("没有需要更新的差异！");
			return;
		}
		// 组装任务消息对象
		GitUpdateTaskMessage updateTaskMessage = new GitUpdateTaskMessage();
		updateTaskMessage.setRepositoryId(id);
		updateTaskMessage.setCommitId(newId);
		updateTaskMessage.setDiffs(ArticleDiff.toArticleDiff(diffs));
		rabbitTemplate.convertAndSend(GIT_TASK_TOPIC_EXCHANGE, GIT_UPDATE, updateTaskMessage);
		log.info("已发布Git仓库更新任务消息至消息队列！");
		// 准备进行邮件通知
		if (sendEmail) {
			// 获取收藏这个文集的用户
			List<User> starUsers = userDAO.getByStarAnthology(id);
			List<String> emailList = starUsers.stream()
					// 过滤得到订阅更新邮件的用户
					.filter(user -> user.getSetting().getReceiveUpdateEmail())
					.map(User::getEmail).toList();
			// 无人订阅通知则不发送消息
			if (emailList.isEmpty()) {
				return;
			}
			// 准备邮件任务消息
			UpdateEmailMessage message = new UpdateEmailMessage();
			message.setTitle("GitDocument · " + systemSettingDAO.get(ORGANIZATION_NAME) + " - 文集更新通知");
			message.setName(showName);
			message.setCommitMessage(gitCommitDAO.getHeadCommit(gitRepository).getFullMessage());
			message.setDiffEntries(ArticleDiff.toArticleDiff(diffs));
			message.setEmailList(emailList);
			// 投递到消息队列
			rabbitTemplate.convertAndSend(EMAIL_TOPIC_EXCHANGE, UPDATE_EMAIL, message);
		}
	}

}
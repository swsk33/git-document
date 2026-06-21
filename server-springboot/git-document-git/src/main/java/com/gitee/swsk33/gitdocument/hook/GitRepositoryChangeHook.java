package com.gitee.swsk33.gitdocument.hook;

import com.gitee.swsk33.gitdocument.gitdao.GitRepositoryDAO;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.transport.PostReceiveHook;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.eclipse.jgit.transport.ReceivePack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

/**
 * 用户推送完成后，执行 Git 任务消息发布的 Hook
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GitRepositoryChangeHook implements PostReceiveHook {

	/**
	 * 操作的Git仓库目录
	 */
	private final String repositoryPath;

	@Autowired
	private GitRepositoryDAO gitRepositoryDAO;

	public GitRepositoryChangeHook(String repositoryPath) {
		this.repositoryPath = repositoryPath;
	}

	@Override
	public void onPostReceive(ReceivePack receivePack, Collection<ReceiveCommand> commands) {
		log.info("接收到仓库：{} 的推送", repositoryPath);
		// 获取第一个 command 提交
		ReceiveCommand receiveCommand = commands.iterator().next();
		// 读取新旧 commitId
		ObjectId oldId = receiveCommand.getOldId();
		ObjectId newId = receiveCommand.getNewId();
		// 如果旧 commitId 不存在或为 zeroId，说明为新建仓库
		if (oldId == null || oldId.equals(ObjectId.zeroId())) {
			log.info("仓库 {} 为新建仓库第一次推送，执行 Git 仓库元数据创建任务", repositoryPath);
			gitRepositoryDAO.doCreateTask(repositoryPath, newId.getName());
		} else {
			// 否则，就是已创建仓库进行更新推送
			log.info("仓库 {} 为更新推送，执行 Git 仓库元数据更新任务", repositoryPath);
			gitRepositoryDAO.doUpdateTask(repositoryPath, oldId.getName(), newId.getName());
		}
		// 最后，打印推送成功消息
		try {
			receivePack.getMessageOutputStream().write("\033[1;36m 推送完成！感谢使用GitDocument！ \033[0m".getBytes());
			receivePack.getMessageOutputStream().flush();
		} catch (IOException e) {
			log.error("执行post-receive钩子出错！");
			log.error(e.getMessage());
		}
	}

}
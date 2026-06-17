package com.gitee.swsk33.gitdocument.command;

import cn.hutool.core.io.FileUtil;
import com.gitee.swsk33.gitdocument.githook.MessagePostReceiveHook;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.command.AbstractCommandSupport;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.ReceivePack;

/**
 * Git的接收命令（客户端的推送操作，从客户端传给服务端）的实现<br>
 * 当客户端执行git push命令时，服务端调用此命令对象
 */
@Slf4j
public class GitReceivePackCommand extends AbstractCommandSupport {

	/**
	 * 操作的Git仓库目录
	 */
	private final String repositoryPath;

	public GitReceivePackCommand(String repositoryPath) {
		super("git-receive-pack", null);
		this.repositoryPath = repositoryPath;
	}

	@Override
	public void run() {
		try (Git repository = Git.open(FileUtil.file(repositoryPath))) {
			// 使用JGit接收客户端数据
			ReceivePack receivePack = new ReceivePack(repository.getRepository());
			receivePack.setPostReceiveHook(new MessagePostReceiveHook());
			receivePack.receive(getInputStream(), getOutputStream(), getErrorStream());
		} catch (Exception e) {
			log.error("从客户端接收数据出错！");
			log.error(e.getMessage());
		} finally {
			onExit(0);
		}
	}

}
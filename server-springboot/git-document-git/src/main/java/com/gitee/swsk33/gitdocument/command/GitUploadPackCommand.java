package com.gitee.swsk33.gitdocument.command;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.command.AbstractCommandSupport;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UploadPack;

/**
 * Git的上传命令（客户端的拉取操作，从服务端传给客户端）的实现<br>
 * 当客户端执行git clone和git pull时，服务端执行此命令对象
 */
@Slf4j
public class GitUploadPackCommand extends AbstractCommandSupport {

	/**
	 * 操作的Git仓库目录
	 */
	private final String repositoryPath;

	public GitUploadPackCommand(String repositoryPath) {
		super("git-upload-pack", null);
		this.repositoryPath = repositoryPath;
	}

	@Override
	public void run() {
		try (Git repository = Git.open(FileUtil.file(repositoryPath)); UploadPack uploadPack = new UploadPack(repository.getRepository())) {
			// 使用JGit传输数据
			uploadPack.upload(getInputStream(), getOutputStream(), getErrorStream());
		} catch (Exception e) {
			log.error("向客户端传输数据出错！");
			log.error(e.getMessage());
		} finally {
			onExit(0);
		}
	}

}
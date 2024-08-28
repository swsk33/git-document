package com.gitee.swsk33.gitdocument.factory;

import com.gitee.swsk33.gitdocument.command.GitReceivePackCommand;
import com.gitee.swsk33.gitdocument.command.GitUploadPackCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.command.CommandFactory;

import java.io.IOException;
import java.util.List;

/**
 * 用于处理Git客户端的拉取和上传请求的命令工厂
 */
@Slf4j
public class GitCommandFactory implements CommandFactory {

	@Override
	public Command createCommand(ChannelSession channel, String command) throws IOException {
		List<String> commandList = CommandFactory.split(command);
		String mainCommand = commandList.getFirst();
		String repositoryPath = commandList.getLast();
		return switch (mainCommand) {
			case "git-upload-pack" -> {
				log.info("向客户端发送数据！同步的仓库为：{}", repositoryPath);
				yield new GitUploadPackCommand(repositoryPath);
			}
			case "git-receive-pack" -> {
				log.info("从客户端接收数据！同步的仓库为：{}", repositoryPath);
				yield new GitReceivePackCommand(repositoryPath);
			}
			default -> {
				log.error("错误！不允许执行命令：{}", command);
				channel.getSession().disconnect(82, String.format("The command is not allowed: '%s'", command));
				yield null;
			}
		};
	}

}
package com.gitee.swsk33.gitdocument.command;

import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.command.AbstractCommandSupport;import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 用户使用ssh -T命令测试连接时，返回的命令操作，仅返回消息，不进行交互式操作
 */
@Slf4j
@Component
public class TestMessageCommand extends AbstractCommandSupport {

	public TestMessageCommand() {
		super(null, null);
	}

	@Override
	public void run() {
		try {
			// 仅返回消息
			String[] messageLines = new String[]{
					String.format("认证成功！\033[1;32m%s\033[0m", getSession().getUsername()),
					"然而，\033[1;36mGitDocument\033[0m不提供交互式ssh会话访问",
					"请使用git命令推送文集仓库",
					""
			};
			getOutputStream().write(ArrayUtil.join(messageLines, "\n").getBytes(StandardCharsets.UTF_8));
			getOutputStream().flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			// 正常退出会话
			getExitCallback().onExit(0);
		}
	}

}
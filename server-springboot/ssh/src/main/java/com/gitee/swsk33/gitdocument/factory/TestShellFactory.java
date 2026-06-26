package com.gitee.swsk33.gitdocument.factory;

import com.gitee.swsk33.gitdocument.command.TestMessageCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.shell.ShellFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用于用户在ssh -T测试连接时，仅返回消息的交互式工厂
 */
@Slf4j
@Component
public class TestShellFactory implements ShellFactory {

	@Autowired
	private TestMessageCommand testMessageCommand;

	@Override
	public Command createShell(ChannelSession channel) {
		return testMessageCommand;
	}

}
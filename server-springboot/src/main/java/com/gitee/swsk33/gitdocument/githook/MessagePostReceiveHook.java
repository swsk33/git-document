package com.gitee.swsk33.gitdocument.githook;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.transport.PostReceiveHook;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.eclipse.jgit.transport.ReceivePack;

import java.io.IOException;
import java.util.Collection;

/**
 * 用户推送完成后，输出消息的钩子
 */
@Slf4j
public class MessagePostReceiveHook implements PostReceiveHook {

	@Override
	public void onPostReceive(ReceivePack receivePack, Collection<ReceiveCommand> commands) {
		try {
			receivePack.getMessageOutputStream().write("\033[1;36m 推送完成！感谢使用GitDocument！ \033[0m".getBytes());
			receivePack.getMessageOutputStream().flush();
		} catch (IOException e) {
			log.error("执行post-receive钩子出错！");
			log.error(e.getMessage());
		}
	}

}
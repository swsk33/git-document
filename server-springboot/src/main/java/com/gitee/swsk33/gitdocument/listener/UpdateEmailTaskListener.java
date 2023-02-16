package com.gitee.swsk33.gitdocument.listener;

import com.gitee.swsk33.gitdocument.message.UpdateEmailMessage;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.service.EmailService;
import com.gitee.swsk33.readandwrite.param.NewLineCharacter;
import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 接收通知任务的消息订阅者
 */
@Component
public class UpdateEmailTaskListener {

	@Autowired
	private EmailService emailService;

	@RabbitListener(queues = CommonValue.MessageQueue.UPDATE_EMAIL_QUEUE_NAME)
	public void getUpdateEmailTask(UpdateEmailMessage message) {
		StringBuilder content = new StringBuilder();
		content.append("您收藏的文集：").append(message.getName()).append("更新了！").append(NewLineCharacter.defaultNewLineChar).append(NewLineCharacter.defaultNewLineChar);
		content.append("提交信息：").append(NewLineCharacter.defaultNewLineChar).append(message.getCommitMessage()).append(NewLineCharacter.defaultNewLineChar).append(NewLineCharacter.defaultNewLineChar);
		content.append("具体变动：").append(NewLineCharacter.defaultNewLineChar);
		List<DiffEntry> entries = message.getDiffEntries();
		for (DiffEntry entry : entries) {
			switch (entry.getChangeType()) {
				case ADD:
					content.append("增加了文章：").append(entry.getNewPath()).append(NewLineCharacter.defaultNewLineChar);
					break;
				case DELETE:
					content.append("删除了文章：").append(entry.getOldPath()).append(NewLineCharacter.defaultNewLineChar);
					break;
				case RENAME:
					content.append("重命名了文章：").append(entry.getOldPath()).append(" -> ").append(entry.getNewPath()).append(NewLineCharacter.defaultNewLineChar);
					break;
				default:
					break;
			}
		}
		emailService.sendNotifyMail(message.getEmail(), message.getTitle(), content.toString());
	}

}
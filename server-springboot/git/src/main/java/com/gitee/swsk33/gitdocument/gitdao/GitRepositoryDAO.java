package com.gitee.swsk33.gitdocument.gitdao;

import com.gitee.swsk33.gitdocument.broker.GitMessageBroker;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;

/**
 * 操作Git裸仓库的接口层
 */
public interface GitRepositoryDAO {

	/**
	 * Git仓库头指针名称
	 */
	String GIT_HEAD_POINTER = "HEAD";

	/**
	 * 创建一个Git裸仓库
	 *
	 * @param gitRepository 仓库文件夹位置（绝对路径）
	 * @return 是否创建成功
	 */
	boolean createGitBareRepository(String gitRepository);

	/**
	 * 当仓库收到第一次提交时，调用此方法将第一次提交的信息作为消息对象传递给 Git 任务消息队列{@link GitMessageBroker}
	 * 由 content 模块订阅者接收并录入数据库完成仓库数据录入操作
	 *
	 * @param repoPath 仓库文件夹位置（绝对路径）
	 * @param newId    仓库提交后新的commitId（从本地仓库读取的commitId）
	 */
	void doCreateTask(String repoPath, String newId);

	/**
	 * 当仓库收到提交且不是第一次提交时，调用此方法对比提交差异信息作为消息对象传递 Git 任务消息队列{@link GitMessageBroker}
	 * 有 content 模块订阅者将信息录入数据库完成仓库数据库元数据更新操作
	 *
	 * @param repoPath  仓库文件夹位置（绝对路径）
	 * @param oldId     仓库更新前的commitId（从数据库中获取的commitId）
	 * @param newId     仓库提交后新的commitId（从本地仓库读取的commitId）
	 * @param sendEmail 是否发送邮件通知
	 */
	void doUpdateTask(String repoPath, String oldId, String newId, boolean sendEmail);

	/**
	 * 检查数据库的仓库对象和其对应的本地仓库的差异，若有差异则发布更新信息
	 *
	 * @param anthology 从数据库查询得到的文集仓库对象，必须要有id属性和repoPath属性不能为空
	 */
	void checkGitRepositoryUpdate(Anthology anthology);

}
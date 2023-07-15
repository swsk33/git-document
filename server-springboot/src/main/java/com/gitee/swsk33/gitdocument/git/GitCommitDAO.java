package com.gitee.swsk33.gitdocument.git;

import com.gitee.swsk33.gitdocument.annotation.GitRepository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;

/**
 * 读取Git裸仓库提交的接口层
 */
@GitRepository
public interface GitCommitDAO {

	/**
	 * 获取一个仓库最新的提交对象
	 *
	 * @param gitRepository 仓库位置（绝对路径）
	 * @return 仓库头指针所在的commit对象，仓库为空返回null
	 */
	RevCommit getHeadCommit(String gitRepository);

	/**
	 * 获取一个仓库最新的提交对象的ObjectId
	 *
	 * @param gitRepository 仓库位置（绝对路径）
	 * @return 仓库头指针所在的commit对象的ObjectId，仓库为空返回null
	 */
	String getHeadCommitId(String gitRepository);

	/**
	 * 获取一个仓库全部的提交对象
	 *
	 * @param gitRepository 仓库位置（绝对路径）
	 * @return 全部提交对象列表，仓库为空返回空列表
	 */
	List<RevCommit> getAllCommits(String gitRepository);

}

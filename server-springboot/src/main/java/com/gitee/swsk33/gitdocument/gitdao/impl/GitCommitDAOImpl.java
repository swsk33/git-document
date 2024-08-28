package com.gitee.swsk33.gitdocument.gitdao.impl;

import cn.hutool.core.io.FileUtil;
import com.gitee.swsk33.gitdocument.gitdao.GitCommitDAO;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.gitee.swsk33.gitdocument.param.CommonValue.GIT_HEAD_POINTER;

@Slf4j
@Component
public class GitCommitDAOImpl implements GitCommitDAO {

	@Override
	public RevCommit getHeadCommit(String gitRepository) {
		// 最新的commit对象
		RevCommit headCommit;
		// 读取仓库
		try (Repository repository = new FileRepositoryBuilder().setGitDir(FileUtil.file(gitRepository)).build()) {
			// 获取头指针指向的提交对象的id
			ObjectId headId = repository.resolve(GIT_HEAD_POINTER);
			if (headId == null) {
				log.warn("仓库：{}没有任何提交！", gitRepository);
				return null;
			}
			headCommit = repository.parseCommit(headId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return headCommit;
	}

	@Override
	public String getHeadCommitId(String gitRepository) {
		RevCommit commit = getHeadCommit(gitRepository);
		return commit != null ? commit.getId().getName() : null;
	}

	@Override
	public List<RevCommit> getAllCommits(String gitRepository) {
		List<RevCommit> result = new ArrayList<>();
		// 读取现有仓库
		// 然后创建遍历对象，可以用于获取所有的commit
		try (Repository repository = new FileRepositoryBuilder().setGitDir(FileUtil.file(gitRepository)).build(); RevWalk walk = new RevWalk(repository)) {
			// 得到HEAD指针的提交ID
			ObjectId headId = repository.resolve(GIT_HEAD_POINTER);
			if (headId == null) {
				log.warn("仓库：{}没有任何提交！", gitRepository);
				return result;
			}
			// 设定从HEAD指针为起始点开始遍历
			walk.markStart(walk.parseCommit(headId));
			// 遍历获取信息，时间从新到旧
			for (RevCommit commit : walk) {
				result.add(commit);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

}
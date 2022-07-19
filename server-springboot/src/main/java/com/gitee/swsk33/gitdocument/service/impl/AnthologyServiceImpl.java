package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.gitee.swsk33.gitdocument.config.ConfigProperties;
import com.gitee.swsk33.gitdocument.context.GitFileListenerContext;
import com.gitee.swsk33.gitdocument.context.GitTaskContext;
import com.gitee.swsk33.gitdocument.dao.AnthologyDAO;
import com.gitee.swsk33.gitdocument.dao.UserDAO;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.model.CommitInfo;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.service.AnthologyService;
import com.gitee.swsk33.gitdocument.service.ImageService;
import com.gitee.swsk33.gitdocument.util.ClassExamine;
import com.gitee.swsk33.gitdocument.util.GitRepositoryUtils;
import com.gitee.swsk33.gitdocument.util.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AnthologyServiceImpl implements AnthologyService {

	@Autowired
	private AnthologyDAO anthologyDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private ImageService imageService;

	@Autowired
	private GitFileListenerContext listenerContext;

	@Autowired
	private ConfigProperties configProperties;

	/**
	 * 启动时，开始监听现有的每个仓库，并开启文件更新任务队列
	 */
	@PostConstruct
	private void startRepositoryUpdateTask() throws Exception {
		// 打开任务队列
		GitTaskContext.getInstance().start();
		List<Anthology> anthologies = anthologyDAO.getAll();
		log.info("共获取到：" + anthologies.size() + "个文集仓库！");
		// 开始监听每个仓库
		for (Anthology anthology : anthologies) {
			listenerContext.addObserver(anthology.getId(), anthology.getRepoPath());
		}
		// 开启监听者
		listenerContext.start();
		log.info("所有仓库位于：" + configProperties.getRepoPath());
	}

	@SaCheckRole(CommonValue.Role.ADMIN)
	@Override
	public Result<Anthology> add(Anthology anthology) {
		Result<Anthology> result = new Result<>();
		Anthology getAnthology = anthologyDAO.getByName(anthology.getName());
		if (getAnthology != null) {
			result.setResultFailed("该文集名已被使用！");
			return result;
		}
		// 先去创建一个Git仓库，并加入到监听列表
		String repoPath = configProperties.getRepoPath() + File.separator + anthology.getName() + ".git";
		if (!GitRepositoryUtils.initGitBareRepository(repoPath)) {
			result.setResultFailed("创建文集仓库失败！请联系开发者！");
			return result;
		}
		log.info("成功创建文集仓库！位于：" + repoPath);
		// 补充信息
		anthology.setId(SnowflakeIdGenerator.getSnowflakeId());
		anthology.setRepoPath(repoPath);
		anthology.setCover(imageService.getRandomCover().getData());
		// 加入监听
		listenerContext.addObserver(anthology.getId(), repoPath);
		// 存入数据库
		anthologyDAO.add(anthology);
		result.setResultSuccess("创建文集仓库成功！");
		return result;
	}

	@SaCheckRole(CommonValue.Role.ADMIN)
	@Override
	public Result<Anthology> delete(long id) throws IOException {
		Result<Anthology> result = new Result<>();
		// 查找仓库
		Anthology getAnthology = anthologyDAO.getById(id);
		if (getAnthology == null) {
			result.setResultFailed("待删除文集不存在！");
			return result;
		}
		// 停止文件监听
		listenerContext.removeObserver(id);
		// 删除仓库文件夹
		FileUtils.deleteDirectory(new File(getAnthology.getRepoPath()));
		if (new File(getAnthology.getRepoPath()).exists()) {
			result.setResultFailed("删除文集仓库失败！请联系开发者！");
			return result;
		}
		log.info("成功删除文集仓库：" + getAnthology.getRepoPath());
		// 从数据库移除
		anthologyDAO.delete(id);
		result.setResultSuccess("删除文集成功！");
		return result;
	}

	@SaCheckRole(CommonValue.Role.ADMIN)
	@Override
	public Result<Anthology> update(Anthology anthology) throws Exception {
		Result<Anthology> result = new Result<>();
		if (!StringUtils.isEmpty(anthology.getLatestCommitId())) {
			result.setResultFailed("不允许手动修改commit id！");
			return result;
		}
		// 补全
		Anthology getAnthology = anthologyDAO.getById(anthology.getId());
		ClassExamine.objectOverlap(anthology, getAnthology);
		anthologyDAO.update(anthology);
		result.setResultSuccess("修改文集信息成功！");
		return result;
	}

	@SaCheckLogin
	@Override
	public Result<Anthology> getById(long id) throws Exception {
		Result<Anthology> result = new Result<>();
		Anthology getAnthology = anthologyDAO.getById(id);
		if (getAnthology == null) {
			result.setResultFailed("该文集不存在！");
			return result;
		}
		// 填充信息
		getAnthology.setSystemUser(CommonValue.RUN_USER_NAME);
		getAnthology.setSshPort(configProperties.getHostPort());
		getAnthology.setUpdateTime(GitRepositoryUtils.getHeadCommit(getAnthology.getRepoPath()).getCommitTime());
		result.setResultSuccess("查找成功！", getAnthology);
		return result;
	}

	@SaCheckLogin
	@Override
	public Result<List<CommitInfo>> getAllCommits(long id) throws Exception {
		Result<List<CommitInfo>> result = new Result<>();
		Anthology getAnthology = anthologyDAO.getById(id);
		if (getAnthology == null) {
			result.setResultFailed("文集不存在！");
			return result;
		}
		List<RevCommit> getCommits = GitRepositoryUtils.getAllCommits(getAnthology.getRepoPath());
		// 结果列表
		List<CommitInfo> commitInfos = new ArrayList<>();
		// 填充信息
		getCommits.forEach(item -> {
			CommitInfo info = new CommitInfo();
			// 获取用户信息
			User getUser = userDAO.getByUsernameOrEmail(item.getAuthorIdent().getEmailAddress());
			if (getUser == null) {
				getUser = new User();
				getUser.setNickname("未知用户");
				getUser.setAvatar(imageService.getRandomAvatar().getData());
			}
			info.setCommitter(getUser);
			info.setMessage(item.getFullMessage());
			info.setTimestamp(item.getCommitTime());
			commitInfos.add(info);
		});
		result.setResultSuccess("获取成功！", commitInfos);
		return result;
	}

	@SaCheckLogin
	@Override
	public Result<List<Anthology>> getAll() {
		Result<List<Anthology>> result = new Result<>();
		List<Anthology> anthologies = anthologyDAO.getAll();
		// 填充信息
		anthologies.forEach(item -> {
			int timestamp = 0;
			try {
				RevCommit commit = GitRepositoryUtils.getHeadCommit(item.getRepoPath());
				if (commit != null) {
					timestamp = commit.getCommitTime();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			item.setSystemUser(CommonValue.RUN_USER_NAME);
			item.setSshPort(configProperties.getHostPort());
			item.setUpdateTime(timestamp);
		});
		result.setResultSuccess("查询成功！", anthologies);
		return result;
	}

}
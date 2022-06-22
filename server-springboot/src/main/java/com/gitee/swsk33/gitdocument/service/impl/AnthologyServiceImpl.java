package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.gitee.swsk33.gitdocument.context.GitTaskContext;
import com.gitee.swsk33.gitdocument.dao.AnthologyDAO;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.service.AnthologyService;
import com.gitee.swsk33.gitdocument.context.GitFileListenerContext;
import com.gitee.swsk33.gitdocument.service.ImageService;
import com.gitee.swsk33.gitdocument.util.GitRepositoryUtils;
import com.gitee.swsk33.gitdocument.util.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class AnthologyServiceImpl implements AnthologyService {

	@Autowired
	private AnthologyDAO anthologyDAO;

	@Autowired
	private ImageService imageService;

	@Autowired
	private GitFileListenerContext listenerContext;

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
		log.info("所有仓库位于：" + CommonValue.ResourcePath.GIT_REPO_PATH);
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
		String repoPath = CommonValue.ResourcePath.GIT_REPO_PATH + File.separator + anthology.getName() + ".git";
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
	public Result<Anthology> update(Anthology anthology) {
		Result<Anthology> result = new Result<>();
		if (!StringUtils.isEmpty(anthology.getLatestCommitId())) {
			result.setResultFailed("不允许手动修改commit id！");
			return result;
		}
		anthologyDAO.update(anthology);
		result.setResultSuccess("修改文集信息成功！");
		return result;
	}

	@SaCheckLogin
	@Override
	public Result<Anthology> getById(long id) {
		Result<Anthology> result = new Result<>();
		Anthology getAnthology = anthologyDAO.getById(id);
		if (getAnthology == null) {
			result.setResultFailed("该文集不存在！");
			return result;
		}
		result.setResultSuccess("查找成功！", getAnthology);
		return result;
	}

	@SaCheckLogin
	@Override
	public Result<List<Anthology>> getAll() {
		Result<List<Anthology>> result = new Result<>();
		List<Anthology> anthologies = anthologyDAO.getAll();
		result.setResultSuccess("查询成功！", anthologies);
		return result;
	}

}
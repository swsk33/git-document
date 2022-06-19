package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.gitee.swsk33.gitdocument.config.GitDocConfigProperties;
import com.gitee.swsk33.gitdocument.dao.AnthologyDAO;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.service.AnthologyService;
import com.gitee.swsk33.gitdocument.util.GitFileListenerUtils;
import com.gitee.swsk33.gitdocument.util.GitRepositoryUtils;
import com.gitee.swsk33.gitdocument.util.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class AnthologyServiceImpl implements AnthologyService {

	@Autowired
	private GitDocConfigProperties configProperties;

	@Autowired
	private AnthologyDAO anthologyDAO;

	@SaCheckRole(CommonValue.Role.ADMIN)
	@Override
	public Result<Anthology> add(Anthology anthology) {
		Result<Anthology> result = new Result<>();
		// 先去创建一个Git仓库，并加入到监听列表
		String repoPath = configProperties.getGitRepoPath() + File.separator + anthology.getName() + ".git";
		if (!GitRepositoryUtils.initGitBareRepository(repoPath)) {
			result.setResultFailed("创建文集仓库失败！请联系开发者！");
			return result;
		}
		// 补充信息
		anthology.setId(SnowflakeIdGenerator.getSnowflakeId());
		anthology.setRepoPath(repoPath);
		// 加入监听
		GitFileListenerUtils.addObserver(anthology.getId(), repoPath);
		// 存入数据库
		anthologyDAO.add(anthology);
		result.setResultSuccess("创建文集仓库成功！");
		return result;
	}

	@SaCheckRole(CommonValue.Role.ADMIN)
	@Override
	public Result<Anthology> delete(long id) {
		Result<Anthology> result = new Result<>();
		// 查找仓库
		Anthology getAnthology = anthologyDAO.getById(id);
		if (getAnthology == null) {
			result.setResultFailed("待删除文集不存在！");
			return result;
		}
		// 停止文件监听
		GitFileListenerUtils.removeObserver(id);
		// 删除仓库文件夹
		if (!new File(getAnthology.getRepoPath()).delete()) {
			result.setResultFailed("删除文集仓库失败！请联系开发者！");
			return result;
		}
		// 从数据库移除
		anthologyDAO.delete(id);
		result.setResultSuccess("删除文集成功！");
		return result;
	}

	@SaCheckRole(CommonValue.Role.ADMIN)
	@Override
	public Result<Anthology> update(Anthology anthology) {
		Result<Anthology> result = new Result<>();
		if (anthologyDAO.update(anthology) < 1) {
			result.setResultFailed("修改文集信息失败！");
			return result;
		}
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
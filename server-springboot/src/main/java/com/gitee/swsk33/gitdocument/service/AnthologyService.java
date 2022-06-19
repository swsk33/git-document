package com.gitee.swsk33.gitdocument.service;

import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.model.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章集（Git仓库）服务
 */
@Service
public interface AnthologyService {

	/**
	 * 添加一个文集
	 *
	 * @param anthology 文集对象
	 */
	Result<Anthology> add(Anthology anthology);

	/**
	 * 删除一个文集
	 *
	 * @param id 文集id
	 */
	Result<Anthology> delete(long id);

	/**
	 * 修改一个文集
	 *
	 * @param anthology 文集对象
	 */
	Result<Anthology> update(Anthology anthology);

	/**
	 * 根据id获取文集
	 *
	 * @param id 文集id
	 */
	Result<Anthology> getById(long id);

	/**
	 * 获取全部文集列表
	 */
	Result<List<Anthology>> getAll();

}
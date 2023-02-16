package com.gitee.swsk33.gitdocument.service;

import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.model.CommitInfo;
import com.gitee.swsk33.gitdocument.model.Result;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
	Result<Void> add(Anthology anthology);

	/**
	 * 批量添加文集
	 *
	 * @param anthologies 文集对象
	 */
	Result<Void> batchAdd(List<Anthology> anthologies);

	/**
	 * 删除一个文集
	 *
	 * @param id 文集id
	 */
	Result<Void> delete(long id) throws IOException;

	/**
	 * 修改一个文集
	 *
	 * @param anthology 文集对象
	 */
	Result<Void> update(Anthology anthology) throws Exception;

	/**
	 * 根据id获取文集
	 *
	 * @param id 文集id
	 */
	Result<Anthology> getById(long id) throws Exception;

	/**
	 * 获取文集全部的贡献对象
	 *
	 * @param id 文集id
	 */
	Result<List<CommitInfo>> getAllCommits(long id) throws Exception;

	/**
	 * 获取全部文集列表
	 */
	Result<List<Anthology>> getAll();

	/**
	 * 获取一个文集仓库中的图片文件
	 *
	 * @param id            文集id
	 * @param imageFilePath 图片文件在文集仓库中的绝对路径
	 */
	Result<byte[]> getImageData(long id, String imageFilePath);

	/**
	 * 获取在本地但是不在数据库中的文集列表
	 *
	 * @return 在本地但是不在数据库中的文集仓库
	 */
	Result<List<Anthology>> getAnthologyNotInDatabase();

}
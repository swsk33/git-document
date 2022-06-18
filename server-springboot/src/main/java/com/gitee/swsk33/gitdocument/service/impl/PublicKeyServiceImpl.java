package com.gitee.swsk33.gitdocument.service.impl;

import com.gitee.swsk33.gitdocument.dao.PublicKeyDAO;
import com.gitee.swsk33.gitdocument.dataobject.PublicKey;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.service.PublicKeyService;
import com.gitee.swsk33.readandwrite.LineScanner;
import com.gitee.swsk33.readandwrite.StringComparer;
import com.gitee.swsk33.readandwrite.TextFileWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

@Slf4j
@Component
public class PublicKeyServiceImpl implements PublicKeyService {

	@Autowired
	private PublicKeyDAO publicKeyDAO;

	/**
	 * authorized_keys文件位置
	 */
	private String publicKeyFilePath;

	@PostConstruct
	private void initPath() throws Exception {
		publicKeyFilePath = System.getProperty("user.home") + File.separator + ".ssh" + File.separator + "authorized_keys";
		File file = new File(publicKeyFilePath);
		if (!file.exists()) {
			log.info("公钥记录文件不存在，将进行创建！");
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		log.info("已找到公钥文件位置：" + publicKeyFilePath);
	}

	@Override
	public Result<PublicKey> add(PublicKey publicKey) throws Exception {
		Result<PublicKey> result = new Result<>();
		if (StringComparer.compareLine(publicKeyFilePath, publicKey.getContent())) {
			result.setResultFailed("待添加的公钥已存在！无需重复添加！");
			return result;
		}
		// 写入公钥到文件中
		if (!TextFileWriter.writeText(publicKeyFilePath, publicKey.getContent() + "\n", "UTF-8")) {
			result.setResultFailed("写入公钥失败！请联系开发者！");
			return result;
		}
		// 得到行数
		publicKey.setLine(LineScanner.getLineCount(publicKeyFilePath));
		// 存入数据库
		publicKeyDAO.add(publicKey);
		result.setResultSuccess("添加公钥成功！");
		return result;
	}

	@Override
	public Result<PublicKey> delete(int id) {

		return null;
	}

	@Override
	public Result<PublicKey> getByUser(int userId) {
		return null;
	}

}
package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.gitee.swsk33.gitdocument.dao.PublicKeyDAO;
import com.gitee.swsk33.gitdocument.dataobject.PublicKey;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.service.PublicKeyService;
import com.gitee.swsk33.readandwrite.LineScanner;
import com.gitee.swsk33.readandwrite.StringComparer;
import com.gitee.swsk33.readandwrite.TextFileReader;
import com.gitee.swsk33.readandwrite.TextFileWriter;
import com.gitee.swsk33.readandwrite.param.CharSetValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;

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

	@SaCheckRole(CommonValue.Role.ADMIN)
	@Override
	public Result<PublicKey> add(PublicKey publicKey) throws Exception {
		Result<PublicKey> result = new Result<>();
		if (StringComparer.compareLine(publicKeyFilePath, publicKey.getContent())) {
			result.setResultFailed("待添加的公钥已存在！无需重复添加！");
			return result;
		}
		if (publicKey.getContent().contains("\r") || publicKey.getContent().contains("\n")) {
			result.setResultFailed("公钥内容不合法！");
			return result;
		}
		// 写入公钥到文件中
		if (!TextFileWriter.appendText(publicKeyFilePath, publicKey.getContent(), CharSetValue.UTF_8)) {
			result.setResultFailed("写入公钥失败！请联系开发者！");
			return result;
		}
		log.info("成功写入公钥至文件：" + publicKeyFilePath);
		// 得到行数
		publicKey.setLine(LineScanner.getTextAtLine(publicKeyFilePath, publicKey.getContent()));
		// 填充用户
		publicKey.setUser((User) StpUtil.getSession().get(CommonValue.SA_USER_SESSION_INFO_KEY));
		// 存入数据库
		publicKeyDAO.add(publicKey);
		result.setResultSuccess("添加公钥成功！");
		return result;
	}

	@SaCheckRole(CommonValue.Role.ADMIN)
	@Override
	public Result<PublicKey> delete(int id) throws Exception {
		Result<PublicKey> result = new Result<>();
		PublicKey getKey = publicKeyDAO.getById(id);
		if (getKey == null) {
			result.setResultFailed("没有这个密钥！");
			return result;
		}
		// 判断当前用户是否是自身
		if (StpUtil.getLoginId(0) != getKey.getUser().getId().intValue()) {
			result.setResultFailed("请勿删除其他用户的公钥！");
			return result;
		}
		TextFileWriter.replaceLine(publicKeyFilePath, getKey.getLine(), "", CharSetValue.UTF_8);
		log.info("移除公钥内容从文件：" + publicKeyFilePath);
		publicKeyDAO.delete(id);
		result.setResultSuccess("移除公钥完成！");
		return result;
	}

	@SaCheckRole(CommonValue.Role.ADMIN)
	@Override
	public Result<List<PublicKey>> getByUser() throws Exception {
		Result<List<PublicKey>> result = new Result<>();
		// 从登录session中获得用户信息
		User getUser = (User) StpUtil.getSession().get(CommonValue.SA_USER_SESSION_INFO_KEY);
		List<PublicKey> publicKeys = publicKeyDAO.getByUserId(getUser.getId());
		for (PublicKey publicKey : publicKeys) {
			publicKey.setContent(TextFileReader.readLine(publicKeyFilePath, publicKey.getLine(), CharSetValue.UTF_8));
		}
		result.setResultSuccess("查询完成！", publicKeys);
		return result;
	}

}
package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.gitee.swsk33.gitdocument.dao.PublicKeyDAO;
import com.gitee.swsk33.gitdocument.dataobject.PublicKey;
import com.gitee.swsk33.gitdocument.dataobject.User;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.CommonValue;
import com.gitee.swsk33.gitdocument.param.PermissionName;
import com.gitee.swsk33.gitdocument.service.PublicKeyService;
import com.gitee.swsk33.readandwrite.LineScanner;
import com.gitee.swsk33.readandwrite.StringComparer;
import com.gitee.swsk33.readandwrite.TextFileReader;
import com.gitee.swsk33.readandwrite.TextFileWriter;
import com.gitee.swsk33.readandwrite.param.CharSetValue;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

	@SaCheckPermission(PermissionName.EDIT_ANTHOLOGY)
	@Override
	public Result<Void> add(PublicKey publicKey) throws Exception {
		if (StringComparer.compareLine(publicKeyFilePath, publicKey.getContent())) {
			return Result.resultFailed("待添加的公钥已存在！无需重复添加！");
		}
		if (publicKey.getContent().contains("\r") || publicKey.getContent().contains("\n")) {
			return Result.resultFailed("公钥内容不合法！");
		}
		// 写入公钥到文件中
		if (!TextFileWriter.appendText(publicKeyFilePath, publicKey.getContent(), CharSetValue.UTF_8)) {
			return Result.resultFailed("写入公钥失败！请联系开发者！");
		}
		log.info("成功写入公钥至文件：" + publicKeyFilePath);
		// 得到行数
		publicKey.setLine(LineScanner.getTextAtLine(publicKeyFilePath, publicKey.getContent()));
		// 填充用户
		publicKey.setUser((User) StpUtil.getSession().get(CommonValue.SA_USER_SESSION_INFO_KEY));
		// 存入数据库
		publicKeyDAO.add(publicKey);
		return Result.resultSuccess("添加公钥成功！");
	}

	@SaCheckPermission(PermissionName.EDIT_ANTHOLOGY)
	@Override
	public Result<Void> delete(int id) throws Exception {
		PublicKey getKey = publicKeyDAO.getById(id);
		if (getKey == null) {
			return Result.resultFailed("没有这个密钥！");
		}
		// 判断当前用户是否是自身
		if (StpUtil.getLoginId(0) != getKey.getUser().getId().intValue()) {
			return Result.resultFailed("请勿删除其他用户的公钥！");
		}
		TextFileWriter.replaceLine(publicKeyFilePath, getKey.getLine(), "", CharSetValue.UTF_8);
		log.info("移除公钥内容从文件：" + publicKeyFilePath);
		publicKeyDAO.delete(id);
		return Result.resultSuccess("移除公钥完成！");
	}

	@SaCheckPermission(PermissionName.EDIT_ANTHOLOGY)
	@Override
	public Result<List<PublicKey>> getByUser() throws Exception {
		// 从登录session中获得用户信息
		User getUser = (User) StpUtil.getSession().get(CommonValue.SA_USER_SESSION_INFO_KEY);
		List<PublicKey> publicKeys = publicKeyDAO.getByUserId(getUser.getId());
		for (PublicKey publicKey : publicKeys) {
			publicKey.setContent(TextFileReader.readLine(publicKeyFilePath, publicKey.getLine(), CharSetValue.UTF_8));
		}
		return Result.resultSuccess("查询完成！", publicKeys);
	}

}
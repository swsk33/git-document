package com.gitee.swsk33.gitdocument.service.impl;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.gitee.swsk33.gitdocument.dao.PublicKeyDAO;
import com.gitee.swsk33.gitdocument.dataobject.PublicKey;
import com.gitee.swsk33.gitdocument.model.Result;
import com.gitee.swsk33.gitdocument.param.PermissionName;
import com.gitee.swsk33.gitdocument.service.PublicKeyService;
import com.gitee.swsk33.gitdocument.util.PublicKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PublicKeyServiceImpl implements PublicKeyService {

	@Autowired
	private PublicKeyDAO publicKeyDAO;

	@SaCheckPermission(PermissionName.EDIT_ANTHOLOGY)
	@Override
	public Result<Void> add(PublicKey publicKey) throws Exception {
		// 处理公钥内容
		String opensshKey = publicKey.getContent().replace("\r", "").replace("\n", "");
		// 转换成PEM的Base64格式
		String pemBase64Key = PublicKeyUtils.opensshPublicKeyToPEM(opensshKey);
		if (StrUtil.isEmpty(pemBase64Key)) {
			return Result.resultFailed("公钥转换失败！请检查公钥是否合法！");
		}
		// 检查重复
		if (publicKeyDAO.existsByContent(pemBase64Key)) {
			return Result.resultFailed("该内容的公钥已存在！");
		}
		// 设定公钥内容
		publicKey.setContent(pemBase64Key);
		// 从session获取当前登录用户id
		publicKey.setUserId(StpUtil.getLoginIdAsInt());
		// 存入数据库
		publicKeyDAO.insert(publicKey);
		return Result.resultSuccess("添加公钥成功！");
	}

	@SaCheckPermission(PermissionName.EDIT_ANTHOLOGY)
	@Override
	public Result<Void> delete(int id) throws Exception {
		PublicKey getKey = publicKeyDAO.selectOneById(id);
		if (getKey == null) {
			return Result.resultFailed("没有这个密钥！");
		}
		// 判断当前用户是否是自身
		if (StpUtil.getLoginIdAsInt() != getKey.getUserId()) {
			return Result.resultFailed("请勿删除其他用户的公钥！");
		}
		publicKeyDAO.deleteById(id);
		return Result.resultSuccess("移除公钥完成！");
	}

}
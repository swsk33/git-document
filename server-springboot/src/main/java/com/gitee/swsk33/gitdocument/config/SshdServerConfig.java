package com.gitee.swsk33.gitdocument.config;

import com.gitee.swsk33.gitdocument.dao.PublicKeyDAO;
import com.gitee.swsk33.gitdocument.factory.GitCommandFactory;
import com.gitee.swsk33.gitdocument.property.SshServerProperties;
import com.gitee.swsk33.gitdocument.util.PublicKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

/**
 * MINA SSHD服务端对象配置
 */
@Slf4j
@Configuration
public class SshdServerConfig {

	@Autowired
	private SshServerProperties sshServerProperties;

	@Autowired
	private PublicKeyDAO publicKeyDAO;

	/**
	 * 配置一个sshd服务端并启动，用于接收Git的远程仓库操作请求
	 *
	 * @return sshd服务端对象
	 */
	@Bean(destroyMethod = "close")
	public SshServer sshdServer() throws Exception {
		// 创建一个ssh服务端对象
		SshServer sshd = SshServer.setUpDefaultServer();
		// 设定端口
		sshd.setPort(sshServerProperties.getEmbedSshServerPort());
		// 设置服务端私钥路径，私钥不存在会自动生成
		sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get("host-key")));
		// 设定为自定义的Git命令工厂实现
		sshd.setCommandFactory(new GitCommandFactory());
		// 设置认证方式，这里使用自定义实现的公钥认证
		sshd.setPublickeyAuthenticator((username, key, session) -> {
			// 获取用户连接的公钥内容
			String clientKeyPEM = PublicKeyUtils.publicKeyToBase64String(key);
			// 判断用户连接的公钥是否存在于数据库
			if (!publicKeyDAO.existsByContent(clientKeyPEM)) {
				log.warn("数据库内没有公钥：{}", clientKeyPEM);
				log.warn("拒绝连接！");
				return false;
			}
			log.info("允许SSH连接！用户名：{}，远程地址：{}", username, session.getClientAddress());
			return true;
		});
		// 启动服务端
		sshd.start();
		log.info("------- sshd服务端，启动！sshd端口号：{} 广播端口号：{} -------", sshServerProperties.getEmbedSshServerPort(), sshServerProperties.getAdvertisedSshServerPort());
		return sshd;
	}

}
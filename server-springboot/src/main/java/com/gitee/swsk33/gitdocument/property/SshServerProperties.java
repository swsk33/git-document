package com.gitee.swsk33.gitdocument.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 关于内嵌SSH服务端的配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "com.gitee.swsk33.git-doc.ssh")
public class SshServerProperties {

	/**
	 * 内嵌ssh服务端端口，内嵌的ssh服务端将使用该端口启动
	 */
	private int embedSshServerPort;

	/**
	 * 广播的ssh服务端端口<br>
	 * 该端口号是广播给客户端（前端）的ssh端口，客户端通过这个端口访问后端内嵌的ssh服务端<br>
	 * 当在物理机部署、不存在多网卡转换环境时，广播端口和实际端口配置为一样即可<br>
	 * 当存在网络转换的环境时，例如在Docker容器环境中，内嵌ssh端口为2333，容器将2333端口映射到宿主机22端口时，广播端口就应该配置为22
	 */
	private int advertisedSshServerPort;

}
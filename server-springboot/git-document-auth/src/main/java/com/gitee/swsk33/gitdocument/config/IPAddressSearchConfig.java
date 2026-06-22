package com.gitee.swsk33.gitdocument.config;

import cn.hutool.core.io.resource.ClassPathResource;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.lionsoul.ip2region.xdb.Version;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

/**
 * IP 地址的查询器配置
 */
@Slf4j
@Configuration
public class IPAddressSearchConfig {

	/**
	 * 全局的 ip 地址查询对象
	 */
	private Searcher searcher;

	@Bean(destroyMethod = "close")
	public Searcher ipSearcher() {
		// 读取 IP 数据库文件
		ClassPathResource resource = new ClassPathResource("ip2region_v4.xdb");
		try (InputStream stream = resource.getStream()) {
			searcher = Searcher.newWithBuffer(Version.IPv4, Searcher.loadContentFromInputStream(stream));
		} catch (Exception e) {
			log.error("读取IP数据库文件出错！IP查询器初始化失败！");
			log.error(e.getMessage());
		}
		log.info("------- IP查询器，启动！ -------");
		return searcher;
	}

}
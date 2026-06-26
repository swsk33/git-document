package com.gitee.swsk33.gitdocument;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@ComponentScan
public class GitDocumentContentApp implements InitializingBean {

	@Override
	public void afterPropertiesSet() {
		log.info("------- 文章内容模块已加载 -------");
	}

}
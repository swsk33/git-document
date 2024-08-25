package com.gitee.swsk33.gitdocument.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * IP实用工具测试
 */
@SpringBootTest
public class IPAddressUtilsTests {

	@Autowired
	private Searcher searcher;

	@Test
	@DisplayName("测试IP地址归属地解析")
	void resolveIPLocation() throws Exception {
		String originResult = searcher.search("58.50.170.21");
		String result = IPAddressUtils.resolveIPLocation(originResult);
		System.out.println(result);
		Assertions.assertEquals("中国-湖北省-宜昌市-电信", result);
	}

}
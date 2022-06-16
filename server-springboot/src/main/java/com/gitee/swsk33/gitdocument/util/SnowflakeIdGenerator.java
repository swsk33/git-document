package com.gitee.swsk33.gitdocument.util;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;

/**
 * 雪花Id生成器
 */
public class SnowflakeIdGenerator {

	/*
	  初始化
	 */
	static {
		YitIdHelper.setIdGenerator(new IdGeneratorOptions((short) 8));
	}

	/**
	 * 生成一个雪花id
	 *
	 * @return 生成的雪花id
	 */
	public static long getSnowflakeId() {
		return YitIdHelper.nextId();
	}

}
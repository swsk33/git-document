package com.gitee.swsk33.gitdocument.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 新建分支时触发的Git更新任务
 */
@Setter
@Getter
@AllArgsConstructor
@Slf4j
public class GitCreateTask implements Runnable {

	/**
	 * 仓库id
	 */
	private long repositoryId;

	/**
	 * 新建的文件列表
	 */
	private List<String> fileList;

	@Override
	public void run() {
		// 把新的文件列表编入数据库
		log.info("新建文件正在编入数据库...");
	}

}
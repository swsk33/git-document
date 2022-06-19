package com.gitee.swsk33.gitdocument.listener;

import com.gitee.swsk33.gitdocument.util.GitRepositoryUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

/**
 * Git仓库头指针监听器
 */
@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class GitRepositoryListener extends FileAlterationListenerAdaptor {

	/**
	 * 仓库id，对应一个被监听的目录
	 */
	private long id;

	/**
	 * 被监听的路径
	 */
	private String path;

	@Override
	public void onStart(FileAlterationObserver observer) {
		super.onStart(observer);
	}

	@Override
	public void onDirectoryCreate(File directory) {

	}

	@Override
	public void onDirectoryChange(File directory) {

	}

	@Override
	public void onDirectoryDelete(File directory) {

	}

	@Override
	public void onFileCreate(File file) {
		log.info("Git分支被创建！");
		try {
			GitRepositoryUtils.repositoryCreateUpdate(id, path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFileChange(File file) {
		log.info("Git HEAD被修改！");
		try {
			GitRepositoryUtils.repositoryChangeUpdate(id, path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFileDelete(File file) {

	}

	@Override
	public void onStop(FileAlterationObserver observer) {
		super.onStop(observer);
	}

}
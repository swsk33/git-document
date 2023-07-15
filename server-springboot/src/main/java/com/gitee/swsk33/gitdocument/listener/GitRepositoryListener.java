package com.gitee.swsk33.gitdocument.listener;

import cn.hutool.core.io.watch.Watcher;
import cn.hutool.core.util.StrUtil;
import com.gitee.swsk33.gitdocument.dao.AnthologyDAO;
import com.gitee.swsk33.gitdocument.dataobject.Anthology;
import com.gitee.swsk33.gitdocument.git.GitCommitDAO;
import com.gitee.swsk33.gitdocument.git.GitRepositoryInfoDAO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * Git仓库头指针监听器<br>
 * Git裸仓库中，头指针记录文件位于：仓库目录/refs/heads中<br>
 * 当新建仓库但是未推送时，该目录为空<br>
 * 进行第一次推送时，会创建一个文件记录头指针<br>
 * 当后续进行推送时，其中的头指针文件会先被删除再重新创建<br>
 * 因此，该监听器的主要原理就是监听上述头指针所在目录变化判断仓库状态，只监听文件修改事件，具体如下：<br>
 * <ul>
 *     <li>如果数据库中记录的commit为空，而读取仓库的commit不为空，则进行创建操作，读取所有文集存入数据库</li>
 *     <li>如果数据库中记录的commit和读取仓库的都不为空且不相同，则视为仓库被改变，进行修改操作</li>
 * </ul>
 */
@Data
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class GitRepositoryListener implements Watcher {

	/**
	 * 仓库id，对应一个被监听的目录
	 */
	private long id;

	/**
	 * Git裸仓库路径（不是监听路径）
	 */
	private String gitRepository;

	@Autowired
	private AnthologyDAO anthologyDAO;

	@Autowired
	private GitCommitDAO gitCommitDAO;

	@Autowired
	private GitRepositoryInfoDAO gitRepositoryInfoDAO;

	@Override
	public void onCreate(WatchEvent<?> watchEvent, Path path) {
		log.info("创建：{}", path.toAbsolutePath());
	}

	@Override
	public void onModify(WatchEvent<?> watchEvent, Path path) {
		log.info("修改：{}", path);
		// 读取本地仓库的commitId
		String newId = gitCommitDAO.getHeadCommitId(gitRepository);
		// 如果本地仓库为空，则不进行任何操作
		if (StrUtil.isEmpty(newId)) {
			log.warn("仓库：{}是空的！", gitRepository);
		}
		// 获取数据库中的commitId
		Anthology getAnthology = anthologyDAO.getById(id);
		String oldId = getAnthology.getLatestCommitId();
		// 若数据库中记录的commitId为空，说明这是第一次推送，执行仓库的创建操作
		if (StrUtil.isEmpty(oldId)) {
			gitRepositoryInfoDAO.doCreateTask(id, gitRepository, newId);
		} else {
			// 否则，进行更新操作
			gitRepositoryInfoDAO.doUpdateTask(id, getAnthology.getShowName(), gitRepository, true, oldId, newId);
		}
	}

	@Override
	public void onDelete(WatchEvent<?> watchEvent, Path path) {
		log.info("删除：{}", path.toAbsolutePath());
	}

	@Override
	public void onOverflow(WatchEvent<?> watchEvent, Path path) {
		log.info("Overflow：{}", path.toAbsolutePath());
	}

}